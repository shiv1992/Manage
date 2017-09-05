package shivang.manage;

/**
 * Class retains latest issued timed by iFormBuilder access token.
 * Created by SHIVVVV on 8/23/2017.
 */
public class Issue_Time {

    private static Long issueTime;

    /**
     * Sets issue time.
     *
     * @param issueTime the issue time
     */
    public static void setIssueTime(Long issueTime) {
        Issue_Time.issueTime = issueTime;
    }

    /**
     * Gets issue time.
     *
     * @return the issue time
     */
    public static Long getIssueTime() {
        return issueTime;
    }


}
