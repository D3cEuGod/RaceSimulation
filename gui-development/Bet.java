// Bet.java
public class Bet {
    private Horse horse;
    private double amount;
    private double odds;               // decimal odds at time of placement
    private boolean settled = false;
    private double payout = 0.0;       // amount returned (including stake) if won

    public Bet(Horse horse, double amount, double odds) {
        this.horse  = horse;
        this.amount = amount;
        this.odds   = odds;
    }
    public Horse getHorse()    { return horse; }
    public double getAmount()  { return amount; }
    public double getOdds()    { return odds; }
    public boolean isSettled() { return settled; }
    public double getPayout()  { return payout; }

    /** Call once after race ends. Winner = winning horse or null if no finish. */
    public void settle(Horse winner) {
        if (settled) return;
        if (winner != null && winner.equals(horse)) {
            // decimal odds: payout = stake * (1 + odds)
            payout = amount * (1.0 + odds);
        } else {
            payout = 0.0;
        }
        settled = true;
    }

    @Override
    public String toString() {
        String status = settled 
            ? String.format("→ %s, bet=%.2f, odds=%.2f:1, payout=%.2f", 
                            horse.getName(), amount, odds, payout)
            : String.format("→ %s, bet=%.2f, odds=%.2f:1 (unsettled)", 
                            horse.getName(), amount, odds);
        return status;
    }
}

