/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package myapp;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;


public class DemoServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    
    String instanceId = "project";
    String databaseId = "os-product-db";
    
    
    // Instantiates a client
    SpannerOptions options = SpannerOptions.newBuilder().build();
    Spanner spanner = options.getService();
    String results = "\n";
    try {
      // Creates a database client
      DatabaseClient dbClient =
          spanner.getDatabaseClient(DatabaseId.of(options.getProjectId(), instanceId, databaseId));
      // Queries the database
      ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of("SELECT *"));

      // Prints the results
      while (resultSet.next()) {
        results += resultSet.getLong(0) + "\n";
      }
    } finally {
      // Closes the client which will free up the resources used
      spanner.close();
    }
  
    resp.setContentType("text/plain");
    resp.getWriter().println("{ \"name\": \"" + results + "\" }");
  }
}
