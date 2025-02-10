Switching to the RDS Data API means that your application no longer connects via a traditional JDBC DataSource; instead, it makes HTTPS calls to the Aurora cluster through the AWS SDK. This has implications for your data access code:

1. **JPA and JDBC vs. Data API:**  
   Standard JPA repositories (like your BlogEntryRepository) rely on a JDBC connection managed by an EntityManager. The RDS Data API, on the other hand, is a RESTful interface and does not support the standard JDBC protocols that JPA depends on.

2. **Code Changes Required:**  
   If you switch to using the Data API, you will not be able to use your existing JPA repositories directly. Instead, you'll have to update your code to use the AWS SDK’s RDS Data API client (for example, `RdsDataClient`) to execute SQL statements. That means rewriting your repository or service methods to build and send SQL commands via the Data API, and then process the JSON (or other structured) responses that you receive.

3. **Alternative Approaches:**  
   - **Hybrid Approach:** You might consider using the Data API for specific cases (like when you need to connect from a serverless function) while continuing to use JDBC in parts of your application.  
   - **Custom Repository Implementation:** You could write a custom repository that uses the RDS Data API client internally. For example, instead of extending `JpaRepository`, you’d implement a repository that calls the Data API to execute queries and then maps the results to your DTOs.

### Example Consideration

For instance, your current repository:

```java
public interface BlogEntryRepository extends JpaRepository<BlogEntry, Long> {

    @Query("SELECT new com.michael_gregory.blog_api.dto.BlogTitle(" +
           "b.id, b.title, b.author, b.createdAt, b.updatedAt) FROM BlogEntry b")
    Page<BlogTitle> findAllTitles(Pageable pageable);
}
```

If you switch to the Data API, you might instead create a service method that:

- Builds the SQL query (e.g., `"SELECT id, title, author, created_at, updated_at FROM blog_entries LIMIT ? OFFSET ?"`).
- Uses the AWS SDK to call the Data API endpoint:
  ```java
  RdsDataClient client = RdsDataClient.builder().region(Region.of("us-east-1")).build();
  ExecuteStatementRequest request = ExecuteStatementRequest.builder()
      .resourceArn("your-cluster-arn")
      .secretArn("your-secret-arn")
      .database("blogdb")
      .sql("SELECT id, title, author, created_at, updated_at FROM blog_entries LIMIT :limit OFFSET :offset")
      .parameters(Parameter.builder().name("limit").value(Field.fromLong(10L)).build(),
                  Parameter.builder().name("offset").value(Field.fromLong(0L)).build())
      .build();
  ExecuteStatementResponse response = client.executeStatement(request);
  // Map the returned records into your BlogTitle DTOs
  ```
- Returns a page of results after mapping.

In summary, **yes, you would need to update your data-access code** if you switch to the RDS Data API. The existing JPA-based repository code won’t work because the Data API does not use JDBC. You’ll have to replace or wrap that functionality using the AWS SDK’s Data API client, which will require changes to how you construct queries, execute them, and map results back to your domain objects.