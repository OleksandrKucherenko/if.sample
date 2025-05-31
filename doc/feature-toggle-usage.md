# Feature Toggle Usage Guide

This document provides examples of using the feature toggle functionality implemented in the insurance service.

## Accessing the Togglz Admin Console

The Togglz admin console is available at:

```
http://localhost:50081/togglz-console
```

Username: `admin`  
Password: `admin`

## Managing Feature Toggles via HTTP

You can use `httpie` to manage feature toggles from the command line:

### Get All Feature Toggle States

```bash
http GET http://localhost:50081/api/features
```

Sample response:
```json
{
  "USE_VEHICLE_SERVICE": true,
  "USE_REDIS_INSURANCE_REPOSITORY": false,
  "GREEN_BLUE_DEPLOYMENT": true
}
```

### Check Deployment Status (for CI/CD)

```bash
http GET http://localhost:50081/api/features/deployment
```

Sample response:
```json
{
  "feature": "GREEN_BLUE_DEPLOYMENT",
  "enabled": true,
  "deploymentType": "BLUE",
  "timestamp": 1622465782123
}
```

### Toggle Features through Togglz API

You can toggle features by sending POST requests to the Togglz State Repository endpoints.
The examples below use basic authentication with the admin credentials:

#### Disable Vehicle Service

```bash
http -a admin:admin POST http://localhost:50081/togglz/state/USE_VEHICLE_SERVICE enabled=false
```

#### Enable Redis Insurance Repository (A/B Testing)

```bash
http -a admin:admin POST http://localhost:50081/togglz/state/USE_REDIS_INSURANCE_REPOSITORY enabled=true
```

#### Toggle Green/Blue Deployment

```bash
http -a admin:admin POST http://localhost:50081/togglz/state/GREEN_BLUE_DEPLOYMENT enabled=false
```

## Use Cases Demonstration

### 1. Enable/Disable Vehicle Service

When `USE_VEHICLE_SERVICE` is enabled:
- The application will call the vehicle service to get vehicle information
- Vehicle data will be included in insurance responses

When `USE_VEHICLE_SERVICE` is disabled:
- No calls will be made to the vehicle service
- Insurance responses will not include vehicle information

### 2. A/B Testing with Different Repositories

When `USE_REDIS_INSURANCE_REPOSITORY` is enabled:
- The application will use RedisInsuranceRepository (in production, this would fetch from Redis)
- Logs will indicate "Using RedisInsuranceRepository for personalId: X"

When `USE_REDIS_INSURANCE_REPOSITORY` is disabled:
- The application will use MockInsuranceRepository
- No special logs will be produced

### 3. Green/Blue Deployment

The `GREEN_BLUE_DEPLOYMENT` toggle can be used by CI/CD pipelines to determine:
- Which deployment environment should be active
- Whether to route traffic to the green or blue deployment

CI/CD scripts can check the status with:
```bash
deployment_type=$(http GET http://localhost:50081/api/features/deployment | jq -r '.deploymentType')
if [ "$deployment_type" = "BLUE" ]; then
  # Route traffic to blue deployment
else
  # Route traffic to green deployment
fi
```
