# Plan: Thu gọn số lượng module common

Mục tiêu: từ **8 module common** hiện tại → **2–3 module** (mục tiêu khuyến nghị: **3**), chấp nhận duplicate code ở service business khi cần.

---

## 1. Inventory & usage thực tế

### 1.1 Bảng dependency (settings.gradle đã include)

| Module | # files (main) | Order | Product | Notification |
|---|---|---|---|---|
| `hdp-common-core` | 31 | ✓ | ✓ | ✓ (transitive) |
| `hdp-common-web` | 22 | ✓ | ✓ | ✓ |
| `hdp-common-infrastructure` | 11 | ✓ | ✓ | ✓ (transitive via web) |
| `hdp-common-persistence` | 12 | ✓ | ✓ | ✗ |
| `hdp-common-messaging` | 9 | ✓ | ✓ | ✗ |
| `hdp-common-observability` | 2 | ✓ | ✗ | ✗ |
| `hdp-common-test` | 6 | declared, **0 usage** | declared, **0 usage** | ✗ |
| `hdp-common-file-storage` | 14 | ✗ | ✓ | ✗ |

### 1.2 Inter-common dependency graph

```
core  (foundation)
 ├── infrastructure
 │    └── web
 ├── persistence
 ├── observability
 └── file-storage  ── test
messaging  (independent)
test       (independent, nhưng dead)
```

---

## 2. Phát hiện quan trọng

### 2.1 Module **chỉ 1 service dùng**

| Module | Service duy nhất | Action |
|---|---|---|
| `hdp-common-observability` | order | **Merge vào `hdp-common-platform`** cùng phase với web+infrastructure. Lý do: là 2 file auto-config thuần infra (actuator + micrometer common tags), không có business logic — merge vào platform để các service khác có thể dùng lại mà không cần duplicate config. |
| `hdp-common-file-storage` | product | **Inline vào `product-service`** (move package `com.hdp.common.filestorage` vào product). Chỉ product dùng. |

→ Cắt **2 module ngay lập tức**.

### 2.2 Module **dead** → xóa

`hdp-common-test`: được declare trong `order-service` và `product-service` build.gradle nhưng **không có file Java nào** trong các service dùng `@ExpectMaxQueryCount` / `QueryCountExtension` / `TestDataFactory` (verify bằng grep — 0 kết quả). Hai service chỉ dùng `spring-boot-starter-test` qua BOM.

→ Xóa module, không cần thay thế. Nếu sau này cần query-count assertion, viết lại inline trong service.

### 2.3 Notification-service — module rất nhẹ

- Chỉ 12 file Java, controller là **stub test virtual-thread pinning** (không phải business code thật).
- Không dùng `persistence`, `messaging`, `file-storage`, `observability`, `test`.
- Vẫn declare `web` + `infrastructure` + `core` nhưng không có file Java nào trong notification-service import trực tiếp từ `com.hdp.common.web.*`. Chỉ có swagger auto-config và exception handler được nạp qua Spring Boot auto-configuration.

→ Khi gộp `web` + `infrastructure` (xem mục 3), notification-service chỉ cần **1 dependency**.

---

## 3. Group theo cohesion → target 3 module

### 3.1 Phân nhóm logic

```
Nhóm "platform/web/infrastructure":  web + infrastructure + observability   (3 module gộp chung)
Nhóm "data/persistence":             persistence (+ test utilities nếu cần sau này)
Nhóm "messaging":                    messaging (Kafka + Avro, độc lập)
```

`messaging` nên giữ riêng vì:
- Có build dependency nặng (Avro compiler plugin, Confluent repo, exclude swagger-annotations conflict).
- Chỉ 2 service dùng (order, product); notification không cần.
- Tách Kafka infra khỏi platform giúp service không dùng Kafka (như notification) không phải kéo thêm dependency.

### 3.2 Target structure (khuyến nghị)

```
hdp-common-core           ← giữ nguyên (foundation: domain primitives, exceptions, usecase, validation, request/filter/sort, util)
hdp-common-platform       ← MERGE: web + infrastructure (rename + merge)
hdp-common-data           ← RENAME: persistence
hdp-common-messaging      ← giữ nguyên
```

**8 → 4 modules** (giảm 50%).

### 3.3 Target structure (aggressive — nếu muốn tối đa)

```
hdp-common-platform       ← MERGE tất cả: core + web + infrastructure + persistence
hdp-common-messaging      ← giữ riêng
```

**8 → 2 modules**. Đạt được khi team đồng ý chấp nhận một "god module" duy nhất cho mọi spring/web/persistence infra.

---

## 4. So sánh trade-off

| Tiêu chí | 4 modules (khuyến nghị) | 2 modules (aggressive) |
|---|---|---|
| Số common module | 4 | 2 |
| Cohesion | Tốt — mỗi module 1 trách nhiệm | Yếu — platform chứa ~80 file |
| Compile speed khi sửa web filter | Cả 3 service phải rebuild platform | Như nhau |
| Dependency của notification | core + platform + messaging(?) | core + platform (+ messaging?) |
| Cognitive load cho người mới | Trung bình | Thấp (ít module) |
| Rủi ro vòng lặp dependency | Thấp | Rất thấp |

---

## 5. Migration steps (giả sử chọn **4 modules**)

### Phase 1 — Xóa module dead & đơn-consumer (zero risk)
1. Xóa `settings.gradle` line: `include 'hdp-common-test'`, `include 'hdp-common-file-storage'`. (giữ observability — sẽ merge vào platform ở Phase 2).
2. Move `hdp-common-file-storage/src/main/java/com/hdp/common/filestorage/**` → `hdp-product-service/src/main/java/com/hdp/common/filestorage/**`.
3. Move `hdp-common-file-storage/src/test/**` → `hdp-product-service/src/test/**`.
4. Xóa `hdp-common-test` khỏi dependencies của order & product (xóa line `implementation project(':hdp-common-test')`).
5. Verify: `./gradlew :hdp-product-service:build :hdp-order-service:build`.

### Phase 2 — Merge web + infrastructure + observability → `hdp-common-platform`
1. Tạo `hdp-common-platform/build.gradle`:
   - `implementation project(':hdp-common-core')` (giữ nguyên từ web)
   - Bỏ `implementation project(':hdp-common-infrastructure')`
   - Thêm các dependency mà infrastructure kéo (`spring-boot-starter-data-redis`, `commons-pool2`, `spring-boot-starter-jackson`).
   - Thêm 2 dependency mới từ observability: `spring-boot-starter-actuator`, `micrometer-core`.
2. `settings.gradle`: `include 'hdp-common-platform'`; xóa `include 'hdp-common-web'`, `include 'hdp-common-infrastructure'`, `include 'hdp-common-observability'`.
3. Copy toàn bộ file trong `hdp-common-web/src/main/java/com/hdp/common/web/**` → `hdp-common-platform/src/main/java/com/hdp/common/web/**`.
4. Copy toàn bộ file trong `hdp-common-infrastructure/src/main/java/com/hdp/common/infrastructure/**` → `hdp-common-platform/src/main/java/com/hdp/common/infrastructure/**`.
5. Copy toàn bộ file trong `hdp-common-observability/src/main/java/com/hdp/observability/**` → `hdp-common-platform/src/main/java/com/hdp/observability/**`.
6. Copy `hdp-common-observability/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` → `hdp-common-platform/src/main/resources/META-INF/spring/...`. **Quan trọng**: nếu platform chưa có file imports này, cần merge thêm entry `CommonTagsConfig` vào file `AutoConfiguration.imports` của platform (nếu đã có các auto-config khác).
7. Trong 3 service: đổi `implementation project(':hdp-common-web')` + `implementation project(':hdp-common-infrastructure')` + `implementation project(':hdp-common-observability')` → `implementation project(':hdp-common-platform')`.
8. `./gradlew clean build` để verify.

### Phase 3 — Rename persistence → `hdp-common-data` (optional, cosmetic)
- Chỉ đổi tên module trong `settings.gradle` + build.gradle của các service, **không đổi package** (`com.hdp.common.persistence.*`).
- Hoặc giữ nguyên tên `hdp-common-persistence` để giảm diff.

### Phase 4 — Verify
1. `./gradlew :hdp-common-platform:publishToMavenLocal :hdp-common-core:publishToMavenLocal :hdp-common-persistence:publishToMavenLocal :hdp-common-messaging:publishToMavenLocal`
2. `./gradlew :hdp-order-service:build :hdp-product-service:build :hdp-notification-service:build`
3. `./gradlew :hdp-order-service:integrationTest` để cover các IT đang pass.

---

## 6. Rủi ro & mitigation

| Rủi ro | Mitigation |
|---|---|
| Merge web+infra tạo god module khó navigate | Giữ 2 package gốc (`com.hdp.common.web.*`, `com.hdp.common.infrastructure.*`) — không gộp Java package |
| File-storage inline làm product-service nặng (~14 file) | Chấp nhận — chỉ product dùng, không ảnh hưởng service khác |
| Test module xóa có thể cần query-count test lại | Reimplement inline trong service nếu thực sự cần (hiện không ai dùng) |
| Notification-service mất swagger/exception handler nếu bỏ platform | KHÔNG bỏ platform — notification vẫn declare nó để giữ auto-config |
| Avro/Kafka plugin chỉ load khi service kéo messaging | OK — notification không kéo messaging nên không bị ảnh hưởng |

---

## 7. Tóm tắt đề xuất cuối

**Khuyến nghị: 4 modules** (Phase 1 + Phase 2 + Phase 3 optional).

```
BEFORE (8)                           AFTER (4)
hdp-common-core                  →   hdp-common-core           (giữ)
hdp-common-web                   →   hdp-common-platform       (merge web+infrastructure)
hdp-common-infrastructure        →   ↳ (xóa, đã merge)
hdp-common-persistence           →   hdp-common-data           (rename, optional)
hdp-common-messaging             →   hdp-common-messaging      (giữ)
hdp-common-observability         →   ↳ (xóa, inline vào order-service)
hdp-common-test                  →   ↳ (xóa, dead code)
hdp-common-file-storage          →   ↳ (xóa, inline vào product-service)
```

**Kết quả**: 8 → 4 module, không cần duplicate logic ở service (chỉ move code đang chỉ-một-service-dùng sang đúng service đó).

| Trước | Sau | Hành động |
|---|---|---|
| hdp-common-core | hdp-common-core | Giữ nguyên |
| hdp-common-web | hdp-common-platform | **Merge** với infrastructure + observability |
| hdp-common-infrastructure | (đã merge) | Xóa |
| hdp-common-observability | (đã merge) | Xóa, code vào platform |
| hdp-common-persistence | hdp-common-data | Rename (optional, có thể giữ tên cũ) |
| hdp-common-messaging | hdp-common-messaging | Giữ nguyên |
| hdp-common-test | (xóa) | Dead code, không consumer nào dùng Java API |
| hdp-common-file-storage | (xóa) | Inline vào product-service |

Nếu team OK với god module, đi tiếp Phase nâng cao để đạt **2 modules** (gộp thêm core + data vào platform). Cần bạn confirm trước khi tôi bắt đầu Phase 1.
