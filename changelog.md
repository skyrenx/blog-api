


### **November 14, 2024**
- **v1 deployed to production**: First working version deployed via AWS lightsail container as API backend for <https://blog.michaelgregory.dev>
- **Added CORS domains via environment variables**: Improved flexibility for configuring allowed origins across environments.
- **Removed CSRF protection**: Decision made to disable CSRF, as the application is stateless and uses JWT authentication.

---

### **November 13, 2024**
- **Spring profiles added**: Introduced `dev` and `prod` profiles for environment-specific configurations.
- **Users can log in**: Completed user authentication with role-based access control.
- **HTTPS security**: Enabled HTTPS as required protocol for all non-health check endpoints.

---

### **November 10, 2024**
- **Role-based access control enhancements**:
  - Added logic to return a `403 Forbidden` response for users without the appropriate roles.
  - Modified JWT to handle role claims as an array instead of a comma-separated string.

---

### **November 9, 2024**
  - Added exception handling filter to start of the security filter chain to handle exceptions from authentication and authorization filters.
- **JWT improvements**:
  - Enhanced JWT to include roles for secure role-based endpoint access.
  - Updated security context to include user roles during authentication.
- **Refactored repository access**: Moved database access logic from the controller layer to the service layer to follow clean architecture principles.
---

### **November 8, 2024**
- **User registration functional**:
  - Implemented `/register` and `/login` endpoints.
  - Saved new users to the database after validation.
- **JWT authentication operational**: Fully implemented JWT-based authentication.

---

### **October**
- **Oct 31, 2024**:
  - Added custom endpoints for `/find-all-titles`, `/blog-entries/id`, and `/latest-blog-entry`.
- **Oct 29, 2024**:
  - Connected to the local development database and set up BlogEntry entity and Repository. Used Spring Data JPA to automatically generate basic CRUD data operations.
  - Set up MySQL database running locally on port 3306 as native installation on windows. (Later I switch to a docker container for the database ) Created blog_entry table on db.
  - Disabled CORS/CSRF protection to simplify development. They will be configured later. 
- **Oct 23, 2024**
  - Initial Spring Boot rest API running locally and serving HTTP on port 8080.
  - Established remote code repository with git and made first commit: https://github.com/skyrenx/blog-api
  - Also, set up local dev tools for windows: Postman, MySQL workbench.
  - Set up local dev tools on WSL Ubuntu: docker desktop (with WSL mode), vscode, git, java.
  - Set up WSL Ubuntu for development on my windows 11 PC.
  - Initial project established using spring boot 3, Java 21, and Maven dependency management.
  - Initialized the project repository using Spring Initializr (https://start.spring.io/)