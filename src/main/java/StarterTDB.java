/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.*;
import java.util.*;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.solver.QLearning;
import org.apache.jena.tdb.solver.stats.Stats;
import org.apache.jena.tdb.solver.stats.StatsCollector;
import org.apache.jena.tdb.solver.stats.StatsResults;

/**
 * Example of the usual way to connect store and issue a query. A description of
 * the connection and store is read from file "sdb.ttl". Use and password come
 * from environment variables SDB_USER and SDB_PASSWORD.
 */

public class StarterTDB {

    final static String statisticsFile = "Statistics.object";
    public static StatsResults statisticsResult = null;

    static public void main(String... argv) throws IOException {

        String queryString = loadQuery("./Query/LUBM/2.sparql");
        Query query = QueryFactory.create(queryString);
        String directory = "TDB";
        Dataset ds = TDBFactory.createDataset(directory);
        Model model = ds.getDefaultModel();

        /**
         * load part of LUBM data into TDB
         */
        // loadData(model, Arrays.asList("./Data/University1.ttl",
        // "./Data/University2.ttl"), "TURTLE");
        // loadData(model, Arrays.asList("./Data/University1.nt",
        // "./Data/University2.nt"), "N-TRIPLE");

        /**
         * execute and get query results
         */
        try (QueryExecution qe = QueryExecutionFactory.create(query, ds)) {
            ResultSet rs = qe.execSelect();
            for (; rs.hasNext();) {
                QuerySolution soln = rs.nextSolution();
                Iterator<String> vars = soln.varNames();
                for (; vars.hasNext();) {
                    String varName = vars.next();
                    System.out.println(varName + ": " + soln.get(varName).toString());
                }
                System.out.println("------------");
            }
        }
    }

    /**
     * add data to TDB and store statistics data into a file
     * 
     * @param model graph model object
     * @param files data files to be loaded
     * @param type  data file type: "TURTLE" or "N-TRIPLE" or "N3"
     */
    static void loadData(Model model, List<String> files, String type) {
        // load data from files
        System.out.println("Starting loading data...");
        for (String file : files) {
            System.out.println("Loading data file: " + file);
            model = model.read(file, type);
        }
        System.out.println("Finished loading data!");
        StatsCollector sc = Stats.gather(model.getGraph());
        QLearning.writeFile(sc.results(), statisticsFile);
        System.out.println("Finished writing statistics data into file: " + statisticsFile);
    }

    /**
     * load query string from file
     * 
     * @param queryFilePath path of sparql query file
     * @return query string
     * @throws IOException
     */
    static String loadQuery(String queryFilePath) throws IOException {
        StringBuilder queryString = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(queryFilePath)));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            queryString.append(line);
            queryString.append("\n");
        }
        bufferedReader.close();
        // System.out.println(queryString.toString());
        return queryString.toString();
    }

}