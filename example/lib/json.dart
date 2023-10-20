var orderiteam ={
    "id": 10564,
    "local_id": null,
    "order_type": "DELIVERY",
    "order_channel": "ONLINE",
    "order_date": "2023-10-20 06:36:35",
    "requester_type": "CONSUMER",
    "requester_id": 4,
    "requester_uuid": "99f37924-2524-43b9-b470-c5ccd79307c3",
    "shipping_address_id": 4033,
    "requested_delivery_timestamp": "2023-10-20 15:40:00",
    "status": "NEW",
    "net_amount": 13.49,
    "discounted_amount": 1.349,
    "delivery_charge": 0,
    "payable_amount": 12.141,
    "payment_type": "CASH",
    "payment_id": null,
    "prescriber_id": null,
    "branch_id": 1002,
    "comment": null,
    "created_at": "2023-10-20T05:36:36.000000Z",
    "updated_at": "2023-10-20T05:36:36.000000Z",
    "order_products": [
        {
            "id": 23411,
            "parent_id": null,
            "order_id": 10564,
            "product_id": 2001,
            "parent_product_id": null,
            "unit": 1,
            "net_amount": 10.99,
            "discountable_amount": 0,
            "comment": null,
            "product": {
                "id": 2001,
                "sort_order": 1,
                "uuid": "whole-chicken",
                "barcode": null,
                "type": "ITEM",
                "short_name": "Whole Chicken",
                "description": null,
                "status": 1,
                "discountable": 1,
                "creator_id": 2,
                "creator_uuid": "98a8d4fb-5b65-4f62-b121-822924b9549c",
                "tags": "Whole Chicken,wholechicken,category,category,grill items,grillitems,",
                "property": {
                    "platform": "BOTH",
                    "epos_category": "grill-items",
                    "is_coupon": "FALSE",
                    "category": "grill-items",
                    "featured": "0"
                },
                "files": [
                    {
                        "id": 1,
                        "product_id": 2001,
                        "file_name": "roast-chicken_1692795352.jpg",
                        "type": "general",
                        "file_path": "/var/www/html/ordering_platform_php/flava/api/public/files/products"
                    }
                ]
            },
            "components": [
                {
                    "id": 23412,
                    "parent_id": 23411,
                    "order_id": 10564,
                    "product_id": 2012,
                    "parent_product_id": 2001,
                    "unit": 1,
                    "net_amount": 2.5,
                    "discountable_amount": 0,
                    "comment": null,
                    "components": [
                        {
                            "id": 23413,
                            "parent_id": 23412,
                            "order_id": 10564,
                            "product_id": 2025,
                            "parent_product_id": 2012,
                            "unit": 1,
                            "net_amount": 0,
                            "discountable_amount": 0,
                            "comment": null,
                            "components": [],
                            "product": {
                                "id": 2025,
                                "sort_order": 12,
                                "uuid": "rubicon-mango",
                                "barcode": null,
                                "type": "COMPONENT",
                                "short_name": "Rubicon Mango",
                                "description": null,
                                "status": 1,
                                "discountable": 1,
                                "creator_id": 2,
                                "creator_uuid": "98a8d4fb-5b65-4f62-b121-822924b9549c",
                                "tags": null,
                                "property": {
                                    "platform": "BOTH",
                                    "is_coupon": "FALSE"
                                }
                            }
                        }
                    ],
                    "product": {
                        "id": 2012,
                        "sort_order": 2,
                        "uuid": "meal-deal-chips",
                        "barcode": null,
                        "type": "COMPONENT",
                        "short_name": "Meal Deal (Chips)",
                        "description": null,
                        "status": 1,
                        "discountable": 1,
                        "creator_id": 2,
                        "creator_uuid": "98a8d4fb-5b65-4f62-b121-822924b9549c",
                        "tags": null,
                        "property": {
                            "platform": "BOTH",
                            "is_coupon": "FALSE"
                        }
                    }
                },
                {
                    "id": 23414,
                    "parent_id": 23411,
                    "order_id": 10564,
                    "product_id": 2011,
                    "parent_product_id": 2001,
                    "unit": 1,
                    "net_amount": 0,
                    "discountable_amount": 0,
                    "comment": null,
                    "components": [],
                    "product": {
                        "id": 2011,
                        "sort_order": 1,
                        "uuid": "none",
                        "barcode": null,
                        "type": "COMPONENT",
                        "short_name": "None",
                        "description": null,
                        "status": 1,
                        "discountable": 1,
                        "creator_id": 2,
                        "creator_uuid": "98a8d4fb-5b65-4f62-b121-822924b9549c",
                        "tags": null,
                        "property": {
                            "platform": "BOTH",
                            "is_coupon": "FALSE"
                        }
                    }
                }
            ]
        },
        {
            "id": 23415,
            "parent_id": null,
            "order_id": 10564,
            "product_id": 2019,
            "parent_product_id": null,
            "unit": 1,
            "net_amount": 1.349,
            "discountable_amount": 0,
            "comment": "discounted",
            "product": {
                "id": 2019,
                "sort_order": 2,
                "uuid": "onallorder",
                "barcode": null,
                "type": "DISCOUNT",
                "short_name": "ONALLORDER",
                "description": "10% OFF on all order",
                "status": 1,
                "discountable": 1,
                "creator_id": 2,
                "creator_uuid": "98a8d4fb-5b65-4f62-b121-822924b9549c",
                "tags": null,
                "property": {
                    "platform": "WEB",
                    "discount_type": "PERCENTAGE",
                    "discount_value": "10",
                    "is_coupon": "FALSE"
                },
                "files": []
            },
            "components": []
        }
    ],
    "requester": {
        "id": 4,
        "uuid": "99f37924-2524-43b9-b470-c5ccd79307c3",
        "role": "CONSUMER",
        "name": "Coby Dominguez",
        "email": "muntasir.yuma@gmail.com",
        "username": "cobyd174",
        "phone": "01889977979",
        "email_verified_at": null,
        "provider": null,
        "provider_id": null,
        "created_at": "2023-08-22T12:11:25.000000Z",
        "updated_at": "2023-08-22T12:21:35.000000Z",
        "property": {
            "first_name": "Coby",
            "last_name": "Dominguez",
            "phone": "01889977979"
        }
    },
    "requester_guest": null,
    "shipping_address": {
        "id": 4033,
        "name": "Shipping Address",
        "type": "SHIPPING",
        "creator_type": "CONSUMER",
        "creator_id": 4,
        "status": 1,
        "created_at": "2023-10-20T05:36:36.000000Z",
        "updated_at": "2023-10-20T05:36:36.000000Z",
        "property": {
            "house": "Unit 27",
            "town": "LUTON",
            "state": "Sedgewick Road",
            "postcode": "LU4 9DT",
            "address": "0",
            "first_name": "",
            "last_name": "",
            "phone": "",
            "dob": "",
            "gender": ""
        }
    },
    "order_files": [],
    "prescriber": null,
    "payment": null,
    "cash_entry": [],
    "branch": {
        "id": 1002,
        "name": "Sundon Park",
        "value": "sundon-park",
        "created_at": null,
        "updated_at": null,
        "property": {
            "phone": "01582354091",
            "email": "contact@theflava.co.uk",
            "address": "4, Ashwell Parade, Luton LU3 3AZ",
            "postcode": "LU3 3AZ"
        }
    }
};

var localorderjson = {"comment":"","createdAt":"2023-10-17T18:19:21.463288","deliveryCharge":0.0,"discountedAmount":0.0,"id":0,"localId":12,"netAmount":25.7,"orderChannel":"EPOS","orderDate":"2023-10-17T18:19:21.463632","orderType":"TAKEOUT","payableAmount":25.7,"paymentId":0,"paymentType":"CASH","prescriberId":null,"requestedDeliveryTimestamp":"2023-10-17T18:34:21.460184","requesterId":0,"requesterType":"PROVIDER","requesterUuid":"","shippingAddressId":0,"status":"NEW","updatedAt":"2023-10-17T18:19:21.463289","items":[{"id":2005,"unit":1,"comment":"","shortName":"Lamb Chops (4pc)","type":"ITEM","currency":"£","price":9.9,"isDiscountApplied":null,"discountPrice":null,"components":[{"id":2012,"shortName":"Meal Deal (Chips)","type":"COMPONENT","groupName":"Choose One","currency":"£","price":2.5,"unit":1,"comment":"","components":{"id":2021,"shortName":"Dr Paper","type":"COMPONENT","groupName":null,"currency":"","price":0.0,"unit":1,"comment":"","components":null}},{"id":2011,"shortName":"None","type":"COMPONENT","groupName":"Flavours","currency":"","price":0.0,"unit":1,"comment":"","components":null},{"id":2016,"shortName":"Mayonnaise Dips","type":"COMPONENT","groupName":"Extra","currency":"£","price":0.2,"unit":1,"comment":"","components":null}],"extra":[]},{"id":2005,"unit":1,"comment":"","shortName":"Lamb Chops (4pc)","type":"ITEM","currency":"£","price":9.9,"isDiscountApplied":null,"discountPrice":null,"components":[{"id":2012,"shortName":"Meal Deal (Chips)","type":"COMPONENT","groupName":"Choose One","currency":"£","price":2.5,"unit":1,"comment":"","components":{"id":2021,"shortName":"Dr Paper","type":"COMPONENT","groupName":null,"currency":"","price":0.0,"unit":1,"comment":"","components":null}},{"id":2011,"shortName":"None","type":"COMPONENT","groupName":"Flavours","currency":"","price":0.0,"unit":1,"comment":"","components":null},{"id":2017,"shortName":"Chili Dips","type":"COMPONENT","groupName":"Extra","currency":"£","price":0.2,"unit":1,"comment":"","components":null}],"extra":[{"id":2136,"shortName":"Extra garlic","type":"ITEM","groupName":null,"currency":"","price":0.0,"unit":1,"comment":null,"components":null},{"id":2138,"shortName":"Extra Hot","type":"ITEM","groupName":null,"currency":"£","price":0.5,"unit":1,"comment":null,"components":null}]}],"customer":{"firstName":"","lastName":"","phone":"","email":"","address":{"type":"","building":"","street":"","city":"","postcode":""}}};
  