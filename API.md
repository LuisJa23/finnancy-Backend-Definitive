# Documentación de API - Finnancy

## Autenticación

### Registrar Usuario
```http
POST /auth/register
Content-Type: application/x-www-form-urlencoded

email=usuario@ejemplo.com&password=mipassword
```

### Iniciar Sesión
```http
POST /auth/login
Content-Type: application/x-www-form-urlencoded

email=usuario@ejemplo.com&password=mipassword
```

**Respuesta:**
```json
{
  "status": "success",
  "message": "User authenticated successfully",
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "uid": "firebase_user_id"
}
```

## Transacciones

### Crear Transacción
```http
POST /api/transactions
Content-Type: application/json

{
  "amount": 100.50,
  "category": "Alimentación",
  "date": "2024-01-15",
  "description": "Compra en supermercado",
  "type": "EXPENSE",
  "expenseType": "VARIABLE",
  "paymentMethod": "CREDIT_CARD",
  "userId": "firebase_user_id"
}
```

### Obtener Transacciones
```http
GET /api/transactions?userId=firebase_user_id&startDate=2024-01-01&endDate=2024-01-31
```

### Resumen Financiero
```http
GET /api/transactions/summary/firebase_user_id
```

**Respuesta:**
```json
{
  "totalBalance": 1500.00,
  "totalIncome": 2000.00,
  "totalExpenses": 500.00,
  "incomeTransactionsCount": 5,
  "expenseTransactionsCount": 10
}
```

## Metas de Ahorro

### Crear Meta
```http
POST /api/savings-goals
Content-Type: application/json

{
  "userId": "firebase_user_id",
  "name": "Vacaciones",
  "targetAmount": 5000.00,
  "targetDate": "2024-12-31"
}
```

### Agregar Fondos
```http
POST /api/savings-goals/{goalId}/add?amount=100.00
```

### Obtener Metas del Usuario
```http
GET /api/savings-goals/user/firebase_user_id
```

## IA Financiera

### Consultar IA
```http
POST /api/ai/ask/firebase_user_id
Content-Type: application/json

{
  "question": "¿Cómo puedo reducir mis gastos mensuales?"
}
```

**Respuesta:**
```json
{
  "answer": "Basándome en tus datos financieros, te recomiendo..."
}
```

## Reportes

### Generar Reporte
```http
GET /api/reports?userId=firebase_user_id&interval=monthly&global=false&type=expense
```

## Códigos de Estado

- `200` - Éxito
- `400` - Error de validación
- `401` - No autorizado
- `404` - Recurso no encontrado
- `500` - Error interno del servidor

## Tipos de Datos

### TransactionType
- `INCOME` - Ingreso
- `EXPENSE` - Gasto

### ExpenseType
- `FIXED` - Gasto fijo
- `VARIABLE` - Gasto variable

### PaymentMethod
- `CASH` - Efectivo
- `CREDIT_CARD` - Tarjeta de crédito
- `DEBIT_CARD` - Tarjeta de débito
- `BANK_TRANSFER` - Transferencia bancaria
- `OTHER` - Otro
