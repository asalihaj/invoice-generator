## Endpoints
### Create Order
Receives a collection of products along with their corresponding 
quantities and generates an order that includes a series of 
invoices, each containing details about the products and their 
quantities.

* Endpoint: `/api/v1/orders`
* Method: `POST`
* Request Body ([example](src/main/resources/data.json))
```json
[
  {
    "product": {
      "name": "Bleach",
      "price": 1.23,
      "discount": 0,
      "vat": 22
    },
    "quantity": 11
  },
  {
    "product": {
      "name": "Aluminum Foil",
      "price": 1.12,
      "discount": 0,
      "vat": 8
    },
    "quantity": 21
  },
  {
    "product": {
      "name": "Razor",
      "price": 8.1,
      "discount": 0,
      "vat": 8
    },
    "quantity": 51
  }
]
```
* Response:
```json
{
  "id": "ORD04",
  "invoices": [
    {
      "id": "INV023",
      "products": [
        {
          "product": {
            "name": "Aluminum Foil",
            "price": 1.12,
            "discount": 0,
            "vat": 8,
            "totalPrice": 1.2096,
            "discountPrice": 1.12
          },
          "quantity": 21,
          "total": 23.52,
          "vat": 1.8816
        },
        {
          "product": {
            "name": "Bleach",
            "price": 1.23,
            "discount": 0,
            "vat": 22,
            "totalPrice": 1.5006,
            "discountPrice": 1.23
          },
          "quantity": 11,
          "total": 13.53,
          "vat": 2.9766
        },
        {
          "product": {
            "name": "Razor",
            "price": 8.1,
            "discount": 0,
            "vat": 8,
            "totalPrice": 8.748,
            "discountPrice": 8.1
          },
          "quantity": 50,
          "total": 405,
          "vat": 32.4
        }
      ],
      "subTotal": 442.05,
      "totalAmount": 479.3082,
      "vat": 37.2582
    },
    {
      "id": "INV024",
      "products": [
        {
          "product": {
            "name": "Razor",
            "price": 8.1,
            "discount": 0,
            "vat": 8,
            "totalPrice": 8.748,
            "discountPrice": 8.1
          },
          "quantity": 1,
          "total": 8.1,
          "vat": 0.648
        }
      ],
      "subTotal": 8.1,
      "totalAmount": 8.748,
      "vat": 0.648
    }
  ],
  "subTotal": 450.15,
  "vat": 37.9062,
  "total": 488.0562
}
```

### Get All Orders
Retrieves a list of all orders.

* Endpoint: `/api/v1/orders`
* Method: `GET`
* Response (example):
```json
[
  {
    "id": "ORD01",
    "totalAmount": 118,
    "subTotal": 100,
    "vat": 18,
    "numberOfInvoices": 2
  }
]

```

### Get Order Details
Retrieves detailed information about a specific order.

* Endpoint: `/api/v1/orders/{id}/details`
* Method: `GET`
* Response (example):
```json
{
    "id": "ORD02",
    "invoices": [
        {
            "id": "INV021",
            "products": [
                {
                    "product": {
                        "name": "Product C",
                        "price": 700.00,
                        "discount": 0,
                        "vat": 15.00,
                        "totalPrice": 805.000,
                        "discountPrice": 700.00
                    },
                    "quantity": 1,
                    "subTotal": 700.00,
                    "vat": 105.0000
                }
            ],
            "subTotal": 700.00,
            "totalAmount": 805.00,
            "vat": 105.00
        }
    ],
    "subTotal": 700.00,
    "vat": 805.00,
    "total": 105.00
}
```

### Delete Order
Deletes a specific order by its ID.

* Endpoint: `/api/v1/orders/{id}`
* Method: `DELETE`
* Response:
```
Order deleted successfully
```
