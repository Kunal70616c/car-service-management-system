# Car Service Management System – Project Flow

## 1) High-level Architecture

The system is built as Spring Boot microservices with separate responsibilities:

- **Eureka Server**: service discovery registry.
- **Gateway Layer**: JWT validation + routing to backend services.
- **Car Service**: main business workflow for service records.
- **User Service**: user profile CRUD + internal existence validation.
- **Car Validation Service**: validates vehicle registration format.
- **Audit Service**: central audit log store.

Each service owns its own database, and services communicate over HTTP.

---

## 2) End-to-End Request Flow (Create Car Service)

1. **Client obtains JWT** from Keycloak (or uses gateway token endpoint in local setup).
2. **Client calls gateway** with `Authorization: Bearer <token>`.
3. **Gateway validates JWT** and routes request to Car Service (via discovery/load balancing).
4. **Car Service controller** receives DTO and maps it to entity.
5. **Car Service business checks** run:
   - service date present and not in future,
   - user exists (calls User Service),
   - no duplicate car registration,
   - car registration valid (calls Car Validation Service).
6. **Car Service persists record** in its own DB.
7. **Car Service emits audit event** to Audit Service (CREATE action with actor + details).
8. **Gateway returns response** to client.

---

## 3) Role and Security Flow

- JWT is validated at gateway and/or resource-server services.
- Method-level authorization enforces scopes:
  - `SCOPE_admin`: full CRUD actions.
  - `SCOPE_user`: read-level access where allowed.
  - `SCOPE_internal_service`: internal endpoint access (e.g., user existence check).
- Car Service can fetch internal service tokens using Keycloak client credentials to call other services securely.

---

## 4) Service Responsibilities in Runtime

### Car Service
- Entry points for create/read/update/delete of car service records.
- Orchestrates calls to User Service, Car Validation Service, and Audit Service.

### User Service
- Manages user profiles.
- Exposes internal endpoint for "does user exist?" checks used by Car Service.

### Car Validation Service
- Validates registration numbers with regex.
- Saves validation history log.

### Audit Service
- Accepts audit events from Car Service.
- Stores and serves audit history.

---

## 5) Startup Sequence

Recommended startup order:

1. Eureka Server
2. User Service
3. Car Validation Service
4. Audit Service
5. Car Service
6. API Gateway

This ensures dependencies are discoverable before orchestration-heavy services start serving traffic.

---

## 6) Practical Summary

Think of **Car Service** as the orchestrator:

- It receives business requests,
- validates domain constraints,
- verifies cross-service dependencies,
- saves core business data,
- and then emits audit trails.

So the flow is: **Auth + Route → Validate dependencies → Persist business record → Audit → Respond**.
