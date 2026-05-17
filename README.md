# 🛒 Sale Campaign Management System

A scalable backend application built using **Java, Spring Boot, Hibernate, and MySQL** to manage large-scale product sale campaigns dynamically.

This project simulates a real-world e-commerce pricing engine where products can participate in multiple sale campaigns, discounts are applied dynamically, and pricing history is maintained for analytics and auditing purposes.

---

## 🚀 Tech Stack

| Technology | Usage |
|---|---|
| Java 17 | Backend Development |
| Spring Boot | REST API Development |
| Spring Data JPA | Database ORM |
| Hibernate | Persistence Layer |
| MySQL | Relational Database |
| Maven | Dependency Management |
| Lombok | Boilerplate Reduction |
| Postman | API Testing |

---

## 📌 Project Objective

A company manages around **100,000+ products** with:
- Product ID
- Product Name
- MRP
- Current Price
- Discount
- Inventory Count

This system enables:
- Creating sale campaigns
- Applying temporary discounts
- Managing campaign lifecycle
- Fetching products with live discount pricing
- Tracking pricing history

---

## ✨ Features

### ✅ Product Management

- Add multiple products
- Store MRP & current price
- Inventory tracking
- Paginated product APIs

---

### ✅ Campaign Management

- Create sale campaigns
- Assign discounts to products
- Automatic campaign status:
  - UPCOMING
  - CURRENT
  - EXPIRED

---

### ✅ Dynamic Pricing Engine

#### Example

| Details | Price |
|---|---|
| MRP | ₹1000 |
| Current Price | ₹900 |
| Campaign Discount | 10% |
| Final Sale Price | ₹810 |

After campaign expiry, product price automatically reverts back.

---

### ✅ Product Pricing History

Stores:
- Product price changes
- Date of change
- Historical pricing records

---

### ✅ Scheduler Support

- Automatically activates campaigns
- Automatically expires campaigns
- Updates product pricing dynamically

---

## 🧠 Business Logic

### Campaign Status Logic

| Condition | Status |
|---|---|
| Current Date < Start Date | UPCOMING |
| Current Date between Start & End Date | CURRENT |
| Current Date > End Date | EXPIRED |

---

## 🗂️ Database Tables

### 1️⃣ Product Table

| Column | Type |
|---|---|
| product_id | Integer |
| product_name | String |
| mrp | Double |
| current_price | Double |
| inventory | Integer |

---

### 2️⃣ Campaign Table

| Column | Type |
|---|---|
| campaign_id | Integer |
| campaign_name | String |
| start_date | LocalDate |
| end_date | LocalDate |
| status | String |

---

### 3️⃣ Campaign_Product Table

| Column | Type |
|---|---|
| id | Integer |
| discount | Integer |
| product_id | FK |
| campaign_id | FK |

---

### 4️⃣ Product_History Table

| Column | Type |
|---|---|
| id | Integer |
| product_id | FK |
| price | Double |
| date | LocalDate |

---

## 🔗 Entity Relationship

```text
Product
   |
   | One-To-Many
   |
CampaignProduct
   |
   | Many-To-One
   |
Campaign

Product
   |
   | One-To-Many
   |
ProductHistory
```


---

## ⚠️ Exception Handling

Custom exception handling is implemented using:
- `@ControllerAdvice`
- `GlobalExceptionHandler`

### 🚨 Custom Exceptions

| Exception | Description |
|---|---|
| InvalidMRPException | Invalid product MRP |
| InvalidInventoryException | Invalid inventory count |
| InvalidDateException | Invalid campaign dates |
| DiscountException | Invalid discount value |
| ProductNotFoundException | Product not found |
| ResourceNotFoundException | Resource not found |

---

# 📡 REST APIs

---

## 🟢 Product APIs

### 1️⃣ Add Products

#### Endpoint

```http
POST /products/add-products
```

#### Description

Adds multiple products into the system.

#### Request Body

```json
{
  "productRequestDTOS": [
    {
      "productName": "iPhone 15",
      "mrp": 80000,
      "currentPrice": 75000,
      "inventory": 50
    },
    {
      "productName": "Samsung S24",
      "mrp": 70000,
      "currentPrice": 68000,
      "inventory": 30
    }
  ]
}
```

#### Success Response

```json
"Products added successfully"
```

#### Status Code

```text
201 CREATED
```

---

### 2️⃣ Get Paginated Products

#### Endpoint

```http
GET /products/get-products-pagination?page=0&pageSize=10
```

#### Description

Fetches products with pagination support.

#### Query Parameters

| Parameter | Type | Description |
|---|---|---|
| page | Integer | Page number |
| pageSize | Integer | Number of records per page |

#### Success Response

```json
{
  "products": [
    {
      "productId": 1,
      "productName": "iPhone 15",
      "mrp": 80000,
      "currentPrice": 75000,
      "inventory": 50
    }
  ],
  "pageNo": 0,
  "pageSize": 10,
  "totalPages": 5
}
```

#### Status Code

```text
200 OK
```

---

### 3️⃣ Get Products With Discount

#### Endpoint

```http
GET /products/products-with-discount
```

#### Description

Returns all products with active campaign discounts applied.

#### Success Response

```json
[
  {
    "productId": 1,
    "productName": "iPhone 15",
    "mrp": 80000,
    "finalPrice": 72000,
    "discount": 10,
    "inventory": 50
  }
]
```

#### Status Code

```text
200 OK
```

---

### 4️⃣ Get Product Price History

#### Endpoint

```http
GET /products/price-history-each-product
```

#### Description

Fetches pricing history of all products.

#### Success Response

```json
[
  {
    "productId": 1,
    "price": 72000,
    "date": "2025-05-17"
  }
]
```

#### Status Code

```text
200 OK
```

---

## 🔵 Campaign APIs

### 1️⃣ Create Campaign

#### Endpoint

```http
POST /campaigns/add-campaign
```

#### Description

Creates a new sale campaign and applies discounts to selected products.

#### Request Body

```json
{
  "campaignName": "Diwali Sale",
  "startDate": "2025-12-10",
  "endDate": "2025-12-15",
  "campaignDiscount": [
    {
      "productId": 1,
      "discount": 10
    },
    {
      "productId": 2,
      "discount": 20
    }
  ]
}
```

#### Success Response

```json
"Campaign is Created."
```

#### Status Code

```text
201 CREATED
```

---

### 2️⃣ Get All Campaigns

#### Endpoint

```http
GET /campaigns/get-all-campaigns
```

#### Description

Returns all campaigns with their current status.

#### Success Response

```json
[
  {
    "campaignId": 1,
    "campaignName": "Diwali Sale",
    "startDate": "2025-12-10",
    "endDate": "2025-12-15",
    "status": "CURRENT"
  }
]
```

#### Status Code

```text
200 OK
```

---

### 3️⃣ Run Scheduler Manually

#### Endpoint

```http
GET /campaigns/run-scheduler
```

#### Description

Manually triggers the campaign scheduler to update campaign statuses and product pricing.

#### Success Response

```json
"Scheduler executed"
```

#### Status Code

```text
200 OK
```

---

## ⚠️ Error Responses

### Example Error Response

```json
{
  "timestamp": "2025-05-17T10:20:30",
  "status": 400,
  "message": "Invalid MRP value"
}
```

---

## 🧪 Validation Rules

| Validation | Rule |
|---|---|
| MRP | Cannot be negative |
| Inventory | Cannot be negative |
| Discount | Must be between 0-100 |
| Start Date | Cannot be after End Date |
| Product List | Cannot be empty |

---

## ▶️ Run Project Locally

### 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/sale-campaign-management-system.git
```

### 2️⃣ Configure Database

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sale_campaign
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Install Dependencies

```bash
mvn clean install
```

### 4️⃣ Run Application

```bash
mvn spring-boot:run
```

---

## 📈 Future Enhancements

- JWT Authentication & Authorization
- Docker Support
- Redis Caching
- Elasticsearch
- Kafka Event Streaming
- Admin Dashboard
- Microservices Architecture
- AWS Deployment

---

# 👨‍💻 Author

## Harsh Chopda

Backend Developer | Java & Spring Boot Developer

### Skills

- Java
- Spring Boot
- Hibernate
- REST APIs
- MySQL
- JPA
- Backend Development

---
