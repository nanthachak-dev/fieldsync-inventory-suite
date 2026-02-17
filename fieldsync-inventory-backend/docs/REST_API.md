## RCRC Seed Management API — REST Documentation

Base URL: `/api`

Authentication
- Login: `POST /api/auth/login`
  - Request body:
    - `username` (string)
    - `password` (string)
  - Response: `{ "token": string }`
  - Use the returned token as needed by the client (no other auth headers detected in code).

Error format
- On errors handled by the global handler:
  - `{ "status": number, "message": string }`

Conventions
- Trailing slashes are accepted.
- Typical CRUD pattern per resource. DTO names indicate request/response shapes.

---

### Auth
- `POST /auth/login` → Login

Example

Request

```json
{
  "username": "admin",
  "password": "secret123"
}
```

Response

```json
{
  "token": "eyJhbGciOi..." 
}
```

### Users
- `POST /app-users` → Create user
- `GET /app-users` → List users
- `GET /app-users/{id}` → Get user by id
- `PUT /app-users/{id}` → Update user
- `DELETE /app-users/{id}` → Delete user

Examples

Create request

```json
{
  "username": "john.doe",
  "password": "P@ssw0rd!",
  "token": null,
  "isActive": true
}
```

Create/Read response

```json
{
  "id": 12,
  "username": "john.doe",
  "password": "********",
  "token": null,
  "isActive": true,
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Roles
- `POST /roles` → Create role
- `GET /roles` → List roles
- `GET /roles/{id}` → Get role
- `PUT /roles/{id}` → Update role
- `DELETE /roles/{id}` → Delete role

Examples

Create/Update request

```json
{
  "name": "ADMIN",
  "description": "Administrator role"
}
```

Read response

```json
{
  "id": 3,
  "name": "ADMIN",
  "description": "Administrator role",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### User Roles
- `POST /user-roles` → Assign role to user
- `GET /user-roles` → List all assignments
- `GET /user-roles/user/{userId}` → List roles by user
- `GET /user-roles/role/{roleId}` → List users by role
- `DELETE /user-roles/{userId}/{roleId}` → Remove assignment

Examples

Assign request

```json
{
  "userId": 12,
  "roleId": 3
}
```

Read response

```json
{
  "appUser": { "id": 12, "username": "john.doe" },
  "role": { "id": 3, "name": "ADMIN" },
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### User Profiles
- `POST /user-profiles` → Create/Update profile
- `GET /user-profiles` → List profiles
- `GET /user-profiles/{userId}` → Get profile by userId
- `DELETE /user-profiles/{userId}` → Delete profile by userId

Example

Request

```json
{
  "userId": 12,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "+1-555-0100"
}
```

Response

```json
{
  "userId": 12,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "+1-555-0100",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z"
}
```

### Inventory — Rice Generations
- `POST /rice-generations` → Create
- `GET /rice-generations` → List
- `GET /rice-generations/{id}` → Get
- `PUT /rice-generations/{id}` → Update
- `DELETE /rice-generations/{id}` → Delete

Examples

Create/Update request

```json
{
  "name": "Foundation",
  "description": "Foundation seed"
}
```

Read response

```json
{
  "id": 2,
  "name": "Foundation",
  "description": "Foundation seed",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Inventory — Rice Varieties
- `POST /rice-varieties` → Create
- `GET /rice-varieties` → List
- `GET /rice-varieties/{id}` → Get
- `PUT /rice-varieties/{id}` → Update
- `DELETE /rice-varieties/{id}` → Delete

Examples

Create/Update request

```json
{
  "name": "IR64",
  "description": "High-yield variety",
  "imageUrl": "https://example.com/ir64.jpg"
}
```

Read response

```json
{
  "id": 3,
  "name": "IR64",
  "description": "High-yield variety",
  "imageUrl": "https://example.com/ir64.jpg",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Inventory — Seasons
- `POST /seasons` → Create
- `GET /seasons` → List
- `GET /seasons/{id}` → Get
- `PUT /seasons/{id}` → Update
- `DELETE /seasons/{id}` → Delete
- `GET /seasons/deleted` → List soft-deleted
- `GET /seasons/all` → List with deleted

Examples

Create/Update request

```json
{
  "name": "Spring",
  "description": "Spring season"
}
```

Read response

```json
{
  "id": 5,
  "name": "Spring",
  "description": "Spring season",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Inventory — Seed Batches
- `POST /seed-batches` → Create
- `GET /seed-batches` → List
- `GET /seed-batches/{id}` → Get
- `PUT /seed-batches/{id}` → Update
- `DELETE /seed-batches/{id}` → Delete

Examples

Create request

```json
{
  "id": null,
  "varietyId": 3,
  "generationId": 2,
  "conditionId": 1,
  "seasonId": 5,
  "year": 2025,
  "grading": true,
  "germination": false,
  "description": "Batch for spring 2025"
}
```

Read response

```json
{
  "id": 101,
  "variety": { "id": 3, "name": "IR64" },
  "generation": { "id": 2, "name": "Foundation" },
  "season": { "id": 5, "name": "Spring" },
  "condition": { "id": 1, "name": "Dry" },
  "year": 2025,
  "grading": true,
  "germination": false,
  "description": "Batch for spring 2025",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Inventory — Seed Conditions
- `POST /seed-conditions` → Create
- `GET /seed-conditions` → List
- `GET /seed-conditions/{id}` → Get
- `PUT /seed-conditions/{id}` → Update
- `DELETE /seed-conditions/{id}` → Delete

Examples

Create/Update request

```json
{
  "name": "Dry",
  "description": "Dry stored seeds"
}
```

Read response

```json
{
  "id": 1,
  "name": "Dry",
  "description": "Dry stored seeds",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Stock — Movements
- `POST /stock-movements` → Create
- `GET /stock-movements` → List
- `GET /stock-movements/{id}` → Get
- `PUT /stock-movements/{id}` → Update
- `DELETE /stock-movements/{id}` → Delete
- `GET /stock-movements/deleted` → List soft-deleted
- `GET /stock-movements/all` → List with deleted

Examples

Create request

```json
{
  "movementTypeId": 1,
  "seedBatchId": 101,
  "stockTransactionId": 5001,
  "quantity": 250.0,
  "description": "Initial receipt"
}
```

Read response

```json
{
  "id": 9001,
  "movementType": { "id": 1, "name": "IN" },
  "seedBatch": { "id": 101 },
  "stockTransaction": { "id": 5001 },
  "quantity": 250.0,
  "description": "Initial receipt",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Stock — Movement Types
- `POST /stock-movement-types` → Create
- `GET /stock-movement-types` → List
- `GET /stock-movement-types/{id}` → Get
- `PUT /stock-movement-types/{id}` → Update
- `DELETE /stock-movement-types/{id}` → Delete

Examples

Create/Update request

```json
{
  "name": "IN",
  "effectOnStock": "INCREASE",
  "description": "Increase stock"
}
```

Read response

```json
{
  "id": 1,
  "name": "IN",
  "effectOnStock": "INCREASE",
  "description": "Increase stock",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Stock — Transactions
- `POST /stock-transactions` → Create
- `GET /stock-transactions` → List
- `GET /stock-transactions/{id}` → Get
- `PUT /stock-transactions/{id}` → Update
- `DELETE /stock-transactions/{id}` → Delete
- `GET /stock-transactions/deleted` → List soft-deleted
- `GET /stock-transactions/all` → List with deleted

Example create request

```json
{
  "transactionTypeId": 2,
  "performedById": 12,
  "transactionDate": "2025-09-24T10:30:00Z",
  "description": "Sale TXN for order SO-1001"
}
```

### Procurement — Purchases
- `POST /purchases` → Create/Upsert
- `GET /purchases` → List
- `GET /purchases/{id}` → Get
- `DELETE /purchases/{id}` → Delete
- `GET /purchases/deleted` → List soft-deleted
- `GET /purchases/all` → List with deleted

Examples

Create request

```json
{
  "stockTransactionId": 8001,
  "supplierId": 21,
  "description": "Purchase from Green Field Co."
}
```

Read response

```json
{
  "stockTransaction": { "id": 8001 },
  "supplier": { "id": 21, "fullName": "Green Field Co." },
  "description": "Purchase from Green Field Co.",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Procurement — Purchase Items
- `POST /purchase-items` → Create/Upsert
- `GET /purchase-items` → List
- `GET /purchase-items/{id}` → Get
- `DELETE /purchase-items/{id}` → Delete
- `GET /purchase-items/deleted` → List soft-deleted
- `GET /purchase-items/all` → List with deleted

Examples

Create request

```json
{
  "stockMovementId": 9101,
  "purchaseId": 8201,
  "price": 30.75,
  "description": "Price per bag"
}
```

Read response

```json
{
  "stockMovement": { "id": 9101 },
  "purchase": { "id": 8201 },
  "price": 30.75,
  "description": "Price per bag",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

Read response

```json
{
  "id": 5001,
  "transactionType": { "id": 2, "name": "SALE" },
  "performedBy": { "id": 12, "username": "john.doe" },
  "transactionDate": "2025-09-24T10:30:00Z",
  "description": "Sale TXN for order SO-1001",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Stock — Transaction Types
- `POST /stock-transaction-types` → Create
- `GET /stock-transaction-types` → List
- `GET /stock-transaction-types/{id}` → Get
- `PUT /stock-transaction-types/{id}` → Update
- `DELETE /stock-transaction-types/{id}` → Delete

Examples

Create/Update request

```json
{
  "name": "SALE",
  "description": "Sale transaction"
}
```

Read response

```json
{
  "id": 2,
  "name": "SALE",
  "description": "Sale transaction",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Stock — Transaction Operations
- `POST /transaction-operations` → Create
- `GET /transaction-operations` → List
- `GET /transaction-operations/{id}` → Get
- `PUT /transaction-operations/{id}` → Update
- `DELETE /transaction-operations/{id}` → Delete

Examples

Create request

```json
{
  "transactionTypeId": 2,
  "performedById": 12,
  "transactionDate": "2025-09-24T10:30:00Z",
  "description": "Sale with movements and sale items",
  "saleRequestDTO": {
    "stockTransactionId": null,
    "customerId": 44,
    "description": "Sale for Acme Farms"
  },
  "stockMovements": [
    {
      "id": null,
      "movementTypeId": 1,
      "seedBatch": {
        "id": null,
        "varietyId": 3,
        "generationId": 2,
        "conditionId": 1,
        "seasonId": 5,
        "year": 2025,
        "grading": true,
        "germination": false,
        "description": "Auto-created batch"
      },
      "saleItem": {
        "stockMovementId": null,
        "saleId": null,
        "price": 45.5,
        "description": "Price per bag"
      },
      "quantity": 250.0,
      "description": "Initial receipt"
    }
  ]
}
```

Read response

```json
{
  "transactionId": 5001,
  "transactionType": { "id": 2, "name": "SALE" },
  "performedBy": { "id": 12, "username": "john.doe" },
  "stockMovements": [
    {
      "id": 9001,
      "movementType": { "id": 1, "name": "IN" },
      "seedBatch": { "id": 101 },
      "saleItem": { "stockMovementId": 9001, "saleId": 7001, "price": 45.5, "description": "Price per bag" },
      "quantity": 250.0,
      "description": "Initial receipt"
    }
  ],
  "transactionDate": "2025-09-24T10:30:00Z",
  "description": "Sale with movements and sale items",
  "sumSale": 11375.0
}
```

### Sales — Customers
- `POST /customers` → Create
- `GET /customers` → List
- `GET /customers/{id}` → Get
- `PUT /customers/{id}` → Update
- `DELETE /customers/{id}` → Delete

Examples

Create request

```json
{
  "customerTypeId": 1,
  "fullName": "Acme Farms",
  "email": "contact@acme.example",
  "phone": "+1-555-0200",
  "address": "100 Market St"
}
```

Read response

```json
{
  "id": 44,
  "fullName": "Acme Farms",
  "email": "contact@acme.example",
  "phone": "+1-555-0200",
  "address": "100 Market St",
  "customerType": {
    "id": 1,
    "name": "Retail",
    "description": "Retail customer",
    "createdAt": "2025-01-01T00:00:00Z",
    "updatedAt": "2025-01-01T00:00:00Z",
    "deletedAt": null
  },
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Sales — Customer Types
- `POST /customer-types` → Create
- `GET /customer-types` → List
- `GET /customer-types/{id}` → Get
- `PUT /customer-types/{id}` → Update
- `DELETE /customer-types/{id}` → Delete

Examples

Create/Update request

```json
{
  "name": "Retail",
  "description": "Retail customer"
}
```

Read response

```json
{
  "id": 1,
  "name": "Retail",
  "description": "Retail customer",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z",
  "deletedAt": null
}
```

### Procurement — Supplier Types
- `POST /supplier-types` → Create
- `GET /supplier-types` → List
- `GET /supplier-types/{id}` → Get
- `PUT /supplier-types/{id}` → Update
- `DELETE /supplier-types/{id}` → Delete

Examples

Create/Update request

```json
{
  "name": "Contract Grower",
  "description": "Suppliers who grow under contract"
}
```

Read response

```json
{
  "id": 10,
  "name": "Contract Grower",
  "description": "Suppliers who grow under contract",
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z",
  "deletedAt": null
}
```

### Procurement — Suppliers
- `POST /suppliers` → Create
- `GET /suppliers` → List
- `GET /suppliers/{id}` → Get
- `PUT /suppliers/{id}` → Update
- `DELETE /suppliers/{id}` → Delete

Examples

Create/Update request

```json
{
  "supplierTypeId": 10,
  "fullName": "Green Field Co.",
  "email": "hello@greenfield.example",
  "phone": "+1-555-0300",
  "address": "200 Farm Lane",
  "description": "Organic supplier"
}
```

Read response

```json
{
  "id": 21,
  "fullName": "Green Field Co.",
  "email": "hello@greenfield.example",
  "phone": "+1-555-0300",
  "address": "200 Farm Lane",
  "description": "Organic supplier",
  "supplierType": { "id": 10, "name": "Contract Grower", "description": "Suppliers who grow under contract" },
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Sales — Sales
- `POST /sales` → Create/Upsert
- `GET /sales` → List
- `GET /sales/{id}` → Get
- `DELETE /sales/{id}` → Delete
- `GET /sales/deleted` → List soft-deleted
- `GET /sales/all` → List with deleted

Examples

Create request

```json
{
  "stockTransactionId": 5001,
  "customerId": 44,
  "description": "Sale for Acme Farms"
}
```

Read response

```json
{
  "stockTransaction": { "id": 5001 },
  "customer": { "id": 44, "fullName": "Acme Farms" },
  "description": "Sale for Acme Farms",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Sales — Sale Items
- `POST /sale-items` → Create/Upsert
- `GET /sale-items` → List
- `GET /sale-items/{id}` → Get
- `DELETE /sale-items/{id}` → Delete
- `GET /sale-items/deleted` → List soft-deleted
- `GET /sale-items/all` → List with deleted

Examples

Create request

```json
{
  "stockMovementId": 9001,
  "saleId": 7001,
  "price": 45.50,
  "description": "Price per bag"
}
```

Read response

```json
{
  "stockMovement": { "id": 9001 },
  "sale": { "id": 7001 },
  "price": 45.50,
  "description": "Price per bag",
  "createdAt": "2025-09-24T10:45:00Z",
  "updatedAt": "2025-09-24T10:45:00Z",
  "deletedAt": null
}
```

### Stock — Movement Details
- `GET /stock-movement-details` → List all details
- `GET /stock-movement-details/{id}` → Get details by Transaction ID
- `GET /stock-movement-details/sync` → Sync updates (returns wrapped records)
- `GET /stock-movement-details/total-stock` → Get total stock quantity
- `GET /stock-movement-details/rice-variety-stock` → Get stock grouped by rice variety (paged)
- `GET /stock-movement-details/top-selling-varieties` → Get top selling rice varieties (paged)

Examples

Read response of `GET /stock-movement-details/{id}` (returns List)

```json
[
  {
    "transactionId": 2,
    "stockMovementId": 6,
    "transactionTypeId": 3,
    "transactionTypeName": "STOCK_OUT",
    "userId": 4,
    "username": "mary.jones",
    "transactionDate": "2025-10-12T10:21:33Z",
    "transactionDescription": "Test edit transaction.",
    "movementTypeId": 5,
    "movementTypeName": "ADJUSTMENT_IN",
    "movementTypeEffectOnStock": "IN",
    "movementTypeDescription": "ປັບປ່ຽນສະພາບແນວພັນ(ເພີ່ມຂຶ້ນ)",
    "stockMovementQuantity": 750.00,
    "stockMovementDescription": "Test edit movement Edit",
    "seedBatchId": 12,
    "seedBatchYear": 2023,
    "seedBatchGrading": true,
    "seedBatchGermination": true,
    "seedBatchDescription": "Test create new Edit",
    "riceVarietyId": 15,
    "riceVarietyName": "CR203",
    "riceVarietyDescription": "Cold-resistant variety",
    "riceVarietyImageUrl": null,
    "generationId": 1,
    "generationName": "R1",
    "generationDescription": "R1",
    "seasonId": 2,
    "seasonName": "WET",
    "seasonDescription": "ຝົນ",
    "saleId": 2,
    "customerId": 2,
    "customerFullName": "Vang Distributors Inc.",
    "saleDescription": "Test create new sale with transaction - Update",
    "saleItemPrice": 700.00,
    "saleItemDescription": "Test sale item 3 - Edit",
    "purchaseId": 5,
    "purchaseDescription": "Test purchase",
    "supplierId": 21,
    "supplierFullName": "Green Field Co.",
    "purchaseItemPrice": 25.00,
    "purchaseItemDescription": "Test purchase item",
    "createdAt": "2025-10-16T17:11:03.177828Z",
    "updatedAt": "2025-10-16T17:11:03.177828Z",
    "deletedAt": null
  }
]
```

Read response of `GET /stock-movement-details/sync?lastSyncTime=2025-10-18T10:31:58Z`

```json
[
  {
    "action": "CREATED",
    "record": {
      "transactionId": 8,
      "stockMovementId": 15,
      "transactionTypeId": 3,
      "transactionTypeName": "STOCK_OUT",
      "userId": 4,
      "username": "mary.jones",
      "transactionDate": "2025-10-12T10:21:33Z",
      "transactionDescription": "Test edit transaction.",
      "movementTypeId": 6,
      "movementTypeName": "SALE",
      "movementTypeEffectOnStock": "OUT",
      "movementTypeDescription": "ຂາຍ",
      "stockMovementQuantity": 550.00,
      "stockMovementDescription": "Test edit movement",
      "seedBatchId": 11,
      "seedBatchYear": 2023,
      "seedBatchGrading": true,
      "seedBatchGermination": true,
      "seedBatchDescription": "Test create new",
      "riceVarietyId": 1,
      "riceVarietyName": "TDK1",
      "riceVarietyDescription": "High-yield variety",
      "riceVarietyImageUrl": null,
      "generationId": 1,
      "generationName": "R1",
      "generationDescription": "R1",
      "seasonId": 1,
      "seasonName": "DRY",
      "seasonDescription": "ແລ້ງ",
      "saleId": 8,
      "customerId": 2,
      "customerFullName": "Vang Distributors Inc.",
      "saleDescription": "Test create new sale with transaction - Update",
      "saleItemPrice": 500.00,
      "saleItemDescription": "Test sale item 1",
      "createdAt": "2025-10-19T13:08:51.167728Z",
      "updatedAt": "2025-10-19T13:08:51.167728Z",
      "deletedAt": null
    }
  }
]
```

Read response of `GET /stock-movement-details/total-stock`

```json
{
  "totalStock": 15000.5,
  "asOfDate": "2025-10-20T10:00:00Z"
}
```

Read response of `GET /stock-movement-details/rice-variety-stock` (Paged)

```json
{
  "content": [
    {
      "riceVarietyId": 1,
      "riceVarietyName": "TDK1",
      "riceVarietyImageUrl": "http://example.com/tdk1.jpg",
      "totalQuantity": 5000.0
    }
  ],
  "page": 0,
  "size": 50,
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

Read response of `GET /stock-movement-details/top-selling-varieties` (Paged)

```json
{
  "content": [
    {
      "riceVarietyId": 1,
      "riceVarietyName": "TDK1",
      "riceVarietyImageUrl": "http://example.com/tdk1.jpg",
      "totalSoldQuantity": 12000.0
    }
  ],
  "page": 0,
  "size": 50,
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

### Stock — Transaction Summaries
- `GET /v1/stock-transaction-summaries` → List summaries (paged or all)
    - Query Params: `startDate` (Instant), `endDate` (Instant), `all` (boolean), `page`, `size`
- `GET /v1/stock-transaction-summaries/count` → Get total transaction count
    - Query Params: `startDate`, `endDate`
- `GET /v1/stock-transaction-summaries/total-sold-out` → Get total quantity sold
    - Query Params: `startDate`, `endDate`

Examples

Read response of `GET /v1/stock-transaction-summaries`

```json
{
  "content": [
    {
      "transactionId": 5001,
      "transactionDate": "2025-09-24T10:30:00Z",
      "transactionTypeId": 2,
      "transactionTypeName": "SALE",
      "transactionDescription": "Sale TXN for order SO-1001",
      "username": "john.doe",
      "mainMovementType": "OUT",
      "itemCount": 5,
      "totalQuantity": 1250.0,
      "totalSalePrice": 56875.0,
      "totalPurchasePrice": null,
      "customerName": "Acme Farms",
      "supplierName": null,
      "fromBatchId": 101,
      "toBatchId": null
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "last": false
}
```

Read response of `GET /v1/stock-transaction-summaries/count`

```json
{
  "totalTransactions": 150,
  "startDate": "2025-01-01T00:00:00Z",
  "endDate": "2025-12-31T23:59:59Z"
}
```

Read response of `GET /v1/stock-transaction-summaries/total-sold-out`

```json
{
  "totalSoldOut": 25000.0,
  "startDate": "2025-01-01T00:00:00Z",
  "endDate": "2025-12-31T23:59:59Z"
}
```

---

Notes
- Request/response DTOs are defined under `src/main/java/.../dto/**`. Field names match those DTOs.
- Soft-deletion awareness endpoints exist for several entities as listed above.


