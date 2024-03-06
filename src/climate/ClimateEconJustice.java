package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered
 * linked list structure that contains USA communitie's Climate and Economic
 * information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;

    /*
     * Constructor
     * 
     * **** DO NOT EDIT *****
     */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
     * Get method to retrieve instance variable firstState
     * 
     * @return firstState
     * 
     * **** DO NOT EDIT *****
     */
    public StateNode getFirstState() {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county,
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     *         **** DO NOT EDIT *****
     */
    public void createLinkedStructure(String inputFile) {

        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();

        // Reads the file one line at a time
        while (StdIn.hasNextLine()) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
     * Adds a state to the first level of the linked structure.
     * Do nothing if the state is already present in the structure.
     * 
     * @param inputLine a line from the input file
     */
    public void addToStateLevel(String inputLine) {
        String[] data = inputLine.split(",");

        StateNode n = new StateNode();
        n.setName(data[2]);
        StateNode ptr = firstState;

        if (firstState == null) {
            firstState = n;
            return;
        }
        if (firstState.getName().equals(data[2]))
            return;

        while (ptr.next != null) {
            if (data[2].equals(ptr.next.getName())) {
                return;
            }
            ptr = ptr.next;
        }
        ptr.next = n;
    }

    // CommTest2_504Communities.csv
    /*
     * Adds a county to a state's list of counties.
     * 
     * Access the state's list of counties' using the down pointer from the State
     * class.
     * Do nothing if the county is already present in the structure.
     * 
     * @param inputFile a line from the input file
     */
    public void addToCountyLevel(String inputLine) {

        String[] data = inputLine.split(",");

        CountyNode county = new CountyNode();
        county.setName(data[1]);
        StateNode ptr = firstState;

        while (ptr != null) {
            if (ptr.getName().equals(data[2])) {
                CountyNode first = ptr.down;
                if (first == null) {
                    ptr.setDown(county);
                    return;
                }

                if (first.getName().equals(data[1]))
                    return;

                while (first.next != null) {
                    if (data[1].equals(first.next.getName()))
                        return;
                    first = first.next;
                }
                first.next = county;
            }
            ptr = ptr.next;
        }
    }

    /*
     * Adds a community to a county's list of communities.
     * 
     * Access the county through its state
     * - search for the state first,
     * - then search for the county.
     * Use the state name and the county name from the inputLine to search.
     * 
     * Access the state's list of counties using the down pointer from the StateNode
     * class.
     * Access the county's list of communities using the down pointer from the
     * CountyNode class.
     * Do nothing if the community is already present in the structure.
     * 
     * @param inputFile a line from the input file
     */
    public void addToCommunityLevel(String inputLine) {

        String[] data = inputLine.split(",");

        CommunityNode cNode = new CommunityNode();
        cNode.setName(data[0]);

        Data d = new Data(Double.parseDouble(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]),
                Double.parseDouble(data[8]), Double.parseDouble(data[9]), data[19], Double.parseDouble(data[49]),
                Double.parseDouble(data[37]), Double.parseDouble(data[121]));

        cNode.setInfo(d);

        StateNode statePtr = firstState;

        while (statePtr != null) {
            if (statePtr.getName().equals(data[2])) {
                CountyNode ctyPtr = statePtr.getDown();
                while (ctyPtr != null) {
                    if (ctyPtr.getName().equals(data[1])) {
                        CommunityNode first = ctyPtr.getDown();
                        if (first == null) {
                            ctyPtr.setDown(cNode);
                            return;
                        }

                        if (first.getName().equals(data[0])) {
                            return;
                        }

                        while (first.next != null) {
                            if (data[0].equals(first.next.getName()))
                                return;
                            first = first.next;
                        }
                        first.next = cNode;
                    }
                    ctyPtr = ctyPtr.next;
                }
            }
            statePtr = statePtr.next;
        }

    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial
     * group
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial
     *                     groups
     * @param race         the race which will be returned
     * @return the amount of communities that contain the same or higher percentage
     *         of the given race
     */
    public int disadvantagedCommunities(double userPrcntage, String race) {

        int count = 0;
        StateNode statePtr = firstState;

        while (statePtr != null) {
            CountyNode countyPtr = statePtr.getDown();
            while (countyPtr != null) {
                CommunityNode communityPtr = countyPtr.getDown();
                while (communityPtr != null) {
                    if (Boolean.parseBoolean(communityPtr.getInfo().getAdvantageStatus())) {
                        double realPercent = 0.0;
                        if (race.equals("African American"))
                            realPercent = communityPtr.getInfo().getPrcntAfricanAmerican() * 100;
                        if (race.equals("Native American"))
                            realPercent = communityPtr.getInfo().getPrcntNative() * 100;
                        if (race.equals("Asian American"))
                            realPercent = communityPtr.getInfo().getPrcntAsian() * 100;
                        if (race.equals("White American"))
                            realPercent = communityPtr.getInfo().getPrcntWhite() * 100;
                        if (race.equals("Hispanic American"))
                            realPercent = communityPtr.getInfo().getPrcntHispanic() * 100;
                        if (realPercent >= userPrcntage)
                            count++;
                    }
                    communityPtr = communityPtr.next;
                }
                countyPtr = countyPtr.next;
            }
            statePtr = statePtr.next;
        }
        return count;
    }

    // TestCommunityData.csv

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial
     * group
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial
     *                     groups
     * @param race         the race which will be returned
     * @return the amount of communities that contain the same or higher percentage
     *         of the given race
     */
    public int nonDisadvantagedCommunities(double userPrcntage, String race) {

        int count = 0;
        StateNode statePtr = firstState;

        while (statePtr != null) {
            CountyNode countyPtr = statePtr.getDown();
            while (countyPtr != null) {
                CommunityNode communityPtr = countyPtr.getDown();
                while (communityPtr != null) {
                    if (!Boolean.parseBoolean(communityPtr.getInfo().getAdvantageStatus())) {
                        double realPercent = 0.0;
                        if (race.equals("African American"))
                            realPercent = communityPtr.getInfo().getPrcntAfricanAmerican() * 100;
                        if (race.equals("Native American"))
                            realPercent = communityPtr.getInfo().getPrcntNative() * 100;
                        if (race.equals("Asian American"))
                            realPercent = communityPtr.getInfo().getPrcntAsian() * 100;
                        if (race.equals("White American"))
                            realPercent = communityPtr.getInfo().getPrcntWhite() * 100;
                        if (race.equals("Hispanic American"))
                            realPercent = communityPtr.getInfo().getPrcntHispanic() * 100;
                        if (realPercent >= userPrcntage)
                            count++;
                    }
                    communityPtr = communityPtr.next;
                }
                countyPtr = countyPtr.next;
            }
            statePtr = statePtr.next;
        }
        return count;
    }

    /**
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */
    public ArrayList<StateNode> statesPMLevels(double PMlevel) {

        ArrayList<StateNode> pmList = new ArrayList<StateNode>();
        StateNode statePtr = firstState;

        while (statePtr != null) {
            CountyNode countyPtr = statePtr.getDown();
            while (countyPtr != null) {
                CommunityNode communityPtr = countyPtr.getDown();
                while (communityPtr != null) {
                    if (communityPtr.getInfo().getPMlevel() >= PMlevel && !pmList.contains(statePtr))
                        pmList.add(statePtr);
                    communityPtr = communityPtr.next;
                }
                countyPtr = countyPtr.next;
            }
            statePtr = statePtr.next;
        }
        return pmList;
    }

    /**
     * Given a percentage inputted by user, returns the number of communities
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood(double userPercntage) {

        int count = 0;
        StateNode statePtr = firstState;

        while (statePtr != null) {
            CountyNode countyPtr = statePtr.getDown();
            while (countyPtr != null) {
                CommunityNode communityPtr = countyPtr.getDown();
                while (communityPtr != null) {
                    if (communityPtr.getInfo().getChanceOfFlood() >= userPercntage)
                        count++;
                    communityPtr = communityPtr.next;
                }
                countyPtr = countyPtr.next;
            }
            statePtr = statePtr.next;
        }
        return count; // replace this line
    }

    /**
     * Given a state inputted by user, returns the communities with
     * the 10 lowest incomes within said state.
     * 
     * @param stateName the State to be analyzed
     * @return the top 10 lowest income communities in the State, with no particular
     *         order
     */
    public ArrayList<CommunityNode> lowestIncomeCommunities(String stateName) {

        ArrayList<CommunityNode> povertyList = new ArrayList<CommunityNode>();
        StateNode statePtr = firstState;
        int minIndex = 0;
        while (statePtr != null) {
            if (stateName.equals(statePtr.getName())) {
                CountyNode countyPtr = statePtr.getDown();
                while (countyPtr != null) {
                    CommunityNode communityPtr = countyPtr.getDown();
                    while (communityPtr != null) {
                        double povertyLevel = communityPtr.getInfo().getPercentPovertyLine();
                        if (povertyList.size() == 10) {
                            for (int i = 0; i < povertyList.size(); i++) {
                                if (povertyList.get(i).getInfo().getPercentPovertyLine() < povertyList.get(minIndex)
                                        .getInfo().getPercentPovertyLine())
                                    minIndex = povertyList.indexOf(povertyList.get(i));
                            }
                            if (povertyLevel > povertyList.get(minIndex).getInfo().getPercentPovertyLine() )
                                    povertyList.set(minIndex, communityPtr);
                        } else {
                            if (!povertyList.contains(communityPtr))
                                povertyList.add(communityPtr);
                        }
                        communityPtr = communityPtr.next;
                    }
                    countyPtr = countyPtr.next;
                }
            }
            statePtr = statePtr.next;
        }
        return povertyList;
    }
}
