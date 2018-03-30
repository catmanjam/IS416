package is416.is416;

/**
 * Created by aispa on 3/27/2018.
 */

public class Rewards {

    private String name;
    private String rewardAmt;
    private String rewardType;

    public Rewards(String name,String rewardAmt,String rewardType){
        this.name = name;
        this.rewardAmt = rewardAmt;
        this.rewardType = rewardType;
    }

    public String getName() {
        return name;
    }

    public String getRewardAmt() {
        return (rewardAmt + " " + rewardType);
    }

    public int getRewardInt() { return Integer.parseInt(rewardAmt); }

}
