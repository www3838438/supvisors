/*
 * Copyright 2016 Julien LE CLEACH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.supvisors.rpc;

import java.net.MalformedURLException;
import java.util.List;
import java.util.HashMap;
import org.apache.xmlrpc.XmlRpcException;
import org.supvisors.common.*;

/**
 * The Class SupvisorsXmlRpc.
 *
 * It uses a SupervisorXmlRpcClient instance to perform XML-RPC requests related to the 'supvisors' namespace.
 * The Javadoc contains extracts from the Supvisors documentation.
 */
public class SupvisorsXmlRpc {

    /** The namespace of System requests in Supervisor. */
    private static final String Namespace = "supvisors.";

    /** The XML-RPC client. */
    private SupervisorXmlRpcClient client;

    /**
     * The constructor keeps a reference to the XML-RPC client.
     *
     * @param SupervisorXmlRpcClient client: The XML-RPC client connected to Supervisor.
     */
    public SupvisorsXmlRpc(final SupervisorXmlRpcClient client)  {
        this.client = client;
    }

    /**
     * The getAPIVersion methods returns the version of the RPC API used by Supvisors.
     *
     * @return String: The version.
     */
    private String getAPIVersion() throws XmlRpcException {
        return client.rpcCall(Namespace + "get_api_version", null, String.class);
    }

    /**
     * The getSupvisorsState methods returns the status of Supvisors.
     *
     * @return SupvisorsStatus: The state of Supvisors.
     */
    public SupvisorsStatus getSupvisorsState() throws XmlRpcException {
        HashMap result = client.rpcCall(Namespace + "get_supvisors_state", null, HashMap.class);
        return new SupvisorsStatus(result);
    }

    /**
     * The getMasterAddress methods returns the address of the Supvisors Master.
     *
     * @return String: A host name.
     */
    public String getMasterAddress() throws XmlRpcException {
        return client.rpcCall(Namespace + "get_master_address", null, String.class);
    }

    /**
     * The getStrategies methods returns the strategies applied in Supvisors.
     *
     * @return SupvisorsStrategies: Information about the strategies.
     */
    public SupvisorsStrategies getStrategies() throws XmlRpcException {
        HashMap result = client.rpcCall(Namespace + "get_strategies", null, HashMap.class);
        return new SupvisorsStrategies(result);
    }

    /**
     * The getAllAddressesInfo methods returns information about the addresses known in Supvisors.
     *
     * @return HashMap<String, SupvisorsAddressInfo>: Information for all address, sorted by name.
     */
    public HashMap<String, SupvisorsAddressInfo> getAllAddressesInfo() throws XmlRpcException {
        Object[] objectsArray = client.rpcCall(Namespace + "get_all_addresses_info", null, Object[].class);
        return DataConversion.arrayToMap(objectsArray, SupvisorsAddressInfo.class);
    }

    /**
     * The getAddressInfo methods returns information about an address known in Supvisors.
     *
     * @param String addressName: The name of the address.
     * @return SupvisorsAddressInfo: Information about the address.
     * @throws XmlRpcException: with code BAD_ADDRESS if addressName is unknown to Supvisors.
     */
    public SupvisorsAddressInfo getAddressInfo(final String addressName) throws XmlRpcException {
        Object[] params = new Object[]{addressName};
        HashMap result = client.rpcCall(Namespace + "get_address_info", params, HashMap.class);
        return new SupvisorsAddressInfo(result);
    }

    /**
     * The getAllApplicationInfo methods returns information about the applications known in Supvisors.
     *
     * @return HashMap<String, SupvisorsApplicationInfo>: Information for all applications, sorted by name.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     */
    public HashMap<String, SupvisorsApplicationInfo> getAllApplicationInfo() throws XmlRpcException {
        Object[] objectsArray = client.rpcCall(Namespace + "get_all_applications_info", null, Object[].class);
        return DataConversion.arrayToMap(objectsArray, SupvisorsApplicationInfo.class);
    }

    /**
     * The getApplicationInfo methods returns information about an application known in Supvisors.
     *
     * @param String applicationName: The name of the application.
     * @return SupvisorsApplicationInfo: Information about the application.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     * @throws XmlRpcException: with code BAD_NAME if applicationName is unknown to Supvisors.
     */
    public SupvisorsApplicationInfo getApplicationInfo(final String applicationName) throws XmlRpcException {
        Object[] params = new Object[]{applicationName};
        HashMap result = client.rpcCall(Namespace + "get_application_info", params, HashMap.class);
        return new SupvisorsApplicationInfo(result);
    }

    /**
     * The getAllProcessInfo methods returns information about all processes known in Supvisors.
     * It just complements the supervisor.getAllProcessInfo by telling where the process is running.
     *
     * @return HashMap<String, SupvisorsProcessInfo>: Information about the processes, sorted by namespec.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     */
    public HashMap<String, SupvisorsProcessInfo> getAllProcessInfo() throws XmlRpcException {
        Object[] objectsArray = client.rpcCall(Namespace + "get_all_process_info", null, Object[].class);
        return DataConversion.arrayToMap(objectsArray, SupvisorsProcessInfo.class);
    }

    /**
     * The getProcessInfo methods returns information about processes known in Supvisors.
     * It just complements the supervisor.getProcessInfo by telling where the process is running.
     *
     * @param String namespec: The name of the process (or "applicationName:processName", or "applicationName:*").
     * @return HashMap<String, SupvisorsProcessInfo>: Information about the process, sorted by namespec.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     * @throws XmlRpcException: with code BAD_NAME if namespec is unknown to Supvisors.
     */
    public HashMap<String, SupvisorsProcessInfo> getProcessInfo(final String namespec) throws XmlRpcException {
        Object[] params = new Object[]{namespec};
        Object[] objectsArray = client.rpcCall(Namespace + "get_process_info", params, Object[].class);
        return DataConversion.arrayToMap(objectsArray, SupvisorsProcessInfo.class);
    }

    /**
     * The getApplicationRules methods returns rules used to start/stop applications known in Supvisors.
     *
     * @param String applicationName: The name of the application.
     * @return SupvisorsApplicationRules: The rules of the application.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     * @throws XmlRpcException: with code BAD_NAME if applicationName is unknown to Supvisors.
     */
    public SupvisorsApplicationRules getApplicationRules(final String applicationName) throws XmlRpcException {
        Object[] params = new Object[]{applicationName};
        HashMap result = client.rpcCall(Namespace + "get_application_rules", params, HashMap.class);
        return new SupvisorsApplicationRules(result);
    }

    /**
     * The getProcessRules methods returns rules used to start/stop processes known in Supvisors.
     *
     * @param String namespec: The name of the process (or "applicationName:processName", or "applicationName:*").
     * @return HashMap<String, SupvisorsProcessRules>: The rules of the processes, sorted by namespec.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     * @throws XmlRpcException: with code BAD_NAME if namespec is unknown to Supvisors.
     */
    public HashMap<String, SupvisorsProcessRules> getProcessRules(final String namespec) throws XmlRpcException {
        Object[] params = new Object[]{namespec};
        Object[] objectsArray = client.rpcCall(Namespace + "get_process_rules", params, Object[].class);
        return DataConversion.arrayToMap(objectsArray, SupvisorsProcessRules.class);
    }

    /**
     * The getConflicts methods returns the conflicting processes.
     *
     * @return HashMap<String, SupvisorsProcessInfo>: The list of conflicting processes, sorted by namespec.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     */
    public HashMap<String, SupvisorsProcessInfo> getConflicts() throws XmlRpcException {
        Object[] objectsArray = client.rpcCall(Namespace + "get_conflicts", null, Object[].class);
        return DataConversion.arrayToMap(objectsArray, SupvisorsProcessInfo.class);
    }

    /**
     * The startApplication methods starts the processes of the application, in accordance with the rules configured
     * in the deployment file for the application and its processes.
     *
     * @param StartingStrategy strategy: The strategy used for choosing addresses.
     * @param String applicationName: The name of the application to start.
     * @param Boolean wait: If true, the RPC returns only when the application is fully started.
     * @return Boolean: Always True unless error or nothing to start.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is not in state OPERATION.
     * @throws XmlRpcException: with code BAD_STRATEGY if strategy is unknown to Supvisors.
     * @throws XmlRpcException: with code BAD_NAME if applicationName is unknown to Supvisors.
     * @throws XmlRpcException: with code ALREADY_STARTED if application is STARTING, STOPPING or RUNNING.
     * @throws XmlRpcException: with code ABNORMAL_TERMINATION if application could not be started.
     */
    public Boolean startApplication(final StartingStrategy strategy, final String applicationName,
            final Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{strategy.ordinal(), applicationName, wait};
        return client.rpcCall(Namespace + "start_application", params, Boolean.class);
    }

    /**
     * The stopApplication methods stops the processes of the application, in accordance with the rules configured
     * in the deployment file for the application and its processes.
     *
     * @param String applicationName: The name of the application to stop.
     * @param Boolean wait: If true, the RPC returns only when the application is fully started.
     * @return Boolean: Always True unless error or nothing to stop.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is not in state OPERATION or CONCILIATION.
     * @throws XmlRpcException: with code BAD_NAME if applicationName is unknown to Supvisors.
     * @throws XmlRpcException: with code NOT_RUNNING if application is STOPPED.
     */
    public Boolean stopApplication(final String applicationName, final Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{applicationName, wait};
        return client.rpcCall(Namespace + "stop_application", params, Boolean.class);
    }

    /**
     * The restartApplication methods restarts the processes of the application, in accordance with the rules configured
     * in the deployment file for the application and its processes.
     *
     * @param StartingStrategy strategy: The strategy used for choosing addresses.
     * @param String applicationName: The name of the application to restart.
     * @param Boolean wait: If true, the RPC returns only when the application is fully restarted.
     * @return Boolean: Always True unless error or nothing to start.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is not in state OPERATION.
     * @throws XmlRpcException: with code BAD_STRATEGY if strategy is unknown to Supvisors.
     * @throws XmlRpcException: with code BAD_NAME if applicationName is unknown to Supvisors.
     * @throws XmlRpcException: with code ABNORMAL_TERMINATION if application could not be restarted.
     */
    public Boolean restartApplication(final StartingStrategy strategy, final String applicationName,
            final Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{strategy.ordinal(), applicationName, wait};
        return client.rpcCall(Namespace + "restart_application", params, Boolean.class);
    }

    /**
     * The startArgs methods starts a process on the local address.
     * The behaviour is different from 'supervisor.startProcess' as it sets the process state to FATAL
     * instead of throwing an exception to the RPC client.
     * This method makes it also possible to pass extra arguments to the program command line.
     *
     * @param String namespec: The name of the process to start.
     * @param String extraArgs: The extra arguments to be passed to the command line of the program.
     * @param Boolean wait: If true, the RPC returns only when the process is fully started.
     * @return Boolean: Always True unless error or nothing to start.
     * @throws XmlRpcException: with code BAD_NAME if namespec is unknown to Supvisors.
     * @throws XmlRpcException: with code BAD_EXTRA_ARGUMENTS if program is required or has a start sequence.
     * @throws XmlRpcException: with code ALREADY_STARTED if process is running.
     * @throws XmlRpcException: with code ABNORMAL_TERMINATION if process could not be started.
     */
    public Boolean startArgs(final String namespec, final String extraArgs, final Boolean wait)
            throws XmlRpcException {
        Object[] params = new Object[]{namespec, extraArgs, wait};
        return client.rpcCall(Namespace + "start_args", params, Boolean.class);
    }

    /**
     * The startProcess methods starts a process, in accordance with the rules ('wait_exit' excepted)
     * configured in the deployment file for the application and its processes.
     * This method makes it also possible to pass extra arguments to the program command line.
     *
     * @param StartingStrategy strategy: The strategy used for choosing addresses.
     * @param String namespec: The name of the process to start.
     * @param String extraArgs: The extra arguments to be passed to the command line of the program.
     * @param Boolean wait: If true, the RPC returns only when the process is fully started.
     * @return Boolean: Always True unless error or nothing to start.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is not in state OPERATION.
     * @throws XmlRpcException: with code BAD_STRATEGY if strategy is unknown to Supvisors.
     * @throws XmlRpcException: with code BAD_NAME if namespec is unknown to Supvisors.
     * @throws XmlRpcException: with code ALREADY_STARTED if process is running.
     * @throws XmlRpcException: with code ABNORMAL_TERMINATION if process could not be started.
     */
    public Boolean startProcess(final StartingStrategy strategy, final String namespec,
            final String extraArgs, final Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{strategy.ordinal(), namespec, extraArgs, wait};
        return client.rpcCall(Namespace + "start_process", params, Boolean.class);
    }

    /**
     * The stopProcess methods stops a process where it is running.
     *
     * @param String namespec: The name of the process to start.
     * @param Boolean wait: If true, the RPC returns only when the process is fully stopped.
     * @return Boolean: Always True unless error or nothing to stop.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is not in state OPERATION or CONCILIATION.
     * @throws XmlRpcException: with code BAD_NAME if namespec is unknown to Supvisors.
     * @throws XmlRpcException: with code NOT_RUNNING if process is stopped.
     */
    public Boolean stopProcess(final String namespec, final Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{namespec, wait};
        return client.rpcCall(Namespace + "stop_process", params, Boolean.class);
    }

    /**
     * The restartProcess methods restarts a process, in accordance with the rules ('wait_exit' excepted)
     * configured in the deployment file for the application and its processes.
     *
     * @param StartingStrategy strategy: The strategy used for choosing addresses.
     * @param String namespec: The name of the process to restart.
     * @param String extraArgs: The extra arguments to be passed to the command line of the program.
     * @param Boolean wait: If true, the RPC returns only when the process is fully restarted.
     * @return Boolean: Always True unless error or nothing to start.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is not in state OPERATION.
     * @throws XmlRpcException: with code BAD_STRATEGY if strategy is unknown to Supvisors.
     * @throws XmlRpcException: with code BAD_NAME if namespec is unknown to Supvisors.
     * @throws XmlRpcException: with code ABNORMAL_TERMINATION if process could not be restarted.
     */
    public Boolean restartProcess(final StartingStrategy strategy, final String namespec,
            final String extraArgs, final Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{strategy.ordinal(), namespec, extraArgs, wait};
        return client.rpcCall(Namespace + "restart_process", params, Boolean.class);
    }

    /**
     * The conciliate methods conciliates process conflicts detected by Supvisors
     * using the strategy in parameter.
     *
     * @param ConciliationStrategy strategy: The strategy used for conciliation.
     * @return Boolean: Always True unless error.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is not in state CONCILIATION.
     * @throws XmlRpcException: with code BAD_STRATEGY if strategy is unknown to Supvisors.
     */
    public Boolean conciliate(final ConciliationStrategy strategy) throws XmlRpcException {
        Object[] params = new Object[]{strategy.ordinal()};
        return client.rpcCall(Namespace + "conciliate", params, Boolean.class);
    }

    /**
     * The restart methods restarts Supvisors through all the Supervisor instances.
     *
     * @return Boolean: Always True unless error.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     */
    public Boolean restart() throws XmlRpcException {
        return client.rpcCall(Namespace + "restart", null, Boolean.class);
    }

    /**
     * The shutdown methods shuts down Supvisors through all the Supervisor instances.
     *
     * @return Boolean: Always True unless error.
     * @throws XmlRpcException: with code BAD_SUPVISORS_STATE if Supvisors is still in INITIALIZATION state,
     */
    public Boolean shutdown() throws XmlRpcException {
        return client.rpcCall(Namespace + "shutdown", null, Boolean.class);
    }


    /**
     * The main for Supvisors self-tests.
     *
     * @param String[] args: The arguments.
     */
    public static void main (String[] args) throws MalformedURLException, XmlRpcException {
        // TODO: add port in parameter of ant script
        SupervisorXmlRpcClient client = new SupervisorXmlRpcClient(60000);
        SupvisorsXmlRpc supvisors = new SupvisorsXmlRpc(client);

        // test supvisors status
        System.out.println("### Testing supvisors.getAPIVersion(...) ###");
        System.out.println(supvisors.getAPIVersion());
        System.out.println("### Testing supvisors.getSupvisorsState(...) ###");
        System.out.println(supvisors.getSupvisorsState());
        System.out.println("### Testing supvisors.getMasterAddress(...) ###");
        System.out.println(supvisors.getMasterAddress());
        System.out.println("### Testing supvisors.getStrategies(...) ###");
        System.out.println(supvisors.getStrategies());

        // test address status rpc
        System.out.println("### Testing supvisors.getAllAddressesInfo(...) ###");
        HashMap<String, SupvisorsAddressInfo> addresses = supvisors.getAllAddressesInfo();
        System.out.println(addresses);
        System.out.println("### Testing supvisors.getAddressInfo(...) ###");
        String addressName = addresses.entrySet().iterator().next().getValue().getName();
        SupvisorsAddressInfo addressInfo = supvisors.getAddressInfo(addressName);
        System.out.println(addressInfo);

        // test application status rpc
        System.out.println("### Testing supvisors.getAllApplicationInfo(...) ###");
        HashMap<String, SupvisorsApplicationInfo> applications = supvisors.getAllApplicationInfo();
        System.out.println(applications);
        System.out.println("### Testing supvisors.getApplicationInfo(...) ###");
        String applicationName = applications.entrySet().iterator().next().getValue().getName();
        SupvisorsApplicationInfo applicationInfo = supvisors.getApplicationInfo(applicationName);
        System.out.println(applicationInfo);

        // test process status rpc
        System.out.println("### Testing supvisors.getAllProcessInfo(...) ###");
        HashMap<String, SupvisorsProcessInfo> processes = supvisors.getAllProcessInfo();
        System.out.println(processes);
        System.out.println("### Testing supvisors.getProcessInfo(...) ###");
        processes = supvisors.getProcessInfo(applicationName + ":*");
        System.out.println(processes);
        String processName = processes.entrySet().iterator().next().getValue().getName();
        processes = supvisors.getProcessInfo(processName);
        System.out.println(processes);

        // test application rules rpc
        System.out.println("### Testing supvisors.getApplicationRules(...) ###");
        SupvisorsApplicationRules applicationRules = supvisors.getApplicationRules(applicationName);
        System.out.println(applicationRules);

        // test process rules rpc
        System.out.println("### Testing supvisors.getProcessRules(...) ###");
        HashMap<String, SupvisorsProcessRules> processRules = supvisors.getProcessRules(applicationName + ":*");
        System.out.println(processRules);
        processName = processRules.entrySet().iterator().next().getValue().getName();
        System.out.println(supvisors.getProcessRules(processName));

        // test process conflicts rpc
        System.out.println("### Testing supvisors.getConflicts(...) ###");
        System.out.println(supvisors.getConflicts());

        // test application request rpc
        System.out.println("### Testing supvisors.restartApplication(...) ###");
        System.out.println(supvisors.restartApplication(StartingStrategy.LESS_LOADED, "my_movies", true));
        System.out.println("### Testing supvisors.stopApplication(...) ###");
        System.out.println(supvisors.stopApplication("my_movies", true));
        System.out.println("### Testing supvisors.startApplication(...) ###");
        System.out.println(supvisors.startApplication(StartingStrategy.CONFIG, "my_movies", false));

        // test process request rpc
        System.out.println("### Testing supvisors.startArgs(...) ###");
        System.out.println(supvisors.startArgs("my_movies:converter_01", "-x 3", false));
        System.out.println("### Testing supvisors.startProcess(...) with no extra args ###");
        System.out.println(supvisors.startProcess(StartingStrategy.MOST_LOADED, "my_movies:converter_02", "", true));
        System.out.println("### Testing supvisors.restartProcess(...) with no extra args ###");
        System.out.println(supvisors.restartProcess(StartingStrategy.CONFIG, "my_movies:converter_02", "", true));
        System.out.println("### Testing supvisors.stopProcess(...) ###");
        System.out.println(supvisors.stopProcess("my_movies:converter_02", false));
        System.out.println("### Testing supvisors.startProcess(...) ###");
        System.out.println(supvisors.startProcess(StartingStrategy.MOST_LOADED, "my_movies:converter_03", "-x 8", true));
        System.out.println("### Testing supvisors.restartProcess(...) ###");
        System.out.println(supvisors.restartProcess(StartingStrategy.LESS_LOADED, "my_movies:converter_03", "-x 4", true));

        // test supvisors request rpc
        System.out.println("### Testing supvisors.conciliate(...) ###");
        try {
            System.out.println(supvisors.conciliate(ConciliationStrategy.RESTART));
        } catch (XmlRpcException e) {
            // expected to fail because there is no conflict
        }
        System.out.println("### Testing supvisors.restart(...) ###");
        System.out.println(supvisors.restart());
        // let a little time to restart before shutdown
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // no matter
        }
        System.out.println("### Testing supvisors.shutdown(...) ###");
        System.out.println(supvisors.shutdown());
    }

}
