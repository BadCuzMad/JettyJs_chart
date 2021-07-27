//import java.lang.reflect.Array;

import java.util.ArrayList;

public class MainRD {

    private ArrayList<Integer> SP;
    private ArrayList<Integer> IP;
    private ArrayList<Integer> DP;
    private int[][] psp;
    private MainPSP[] gen_psp;
    private int[] Len;
    private int[] FB;
    private int[] Strt;
    private int[] Size;

    public MainRD (int[] reg_length, int[] feedback, int[] start, int[] psp_length) {
        this.Len = reg_length;
        this.FB = feedback;
        this.Strt = start;
        this.Size = psp_length;
        gen_psp = new MainPSP[3];
        MakeSPIP(reg_length, feedback, start, psp_length); // 3 ПСП
    }

    public int[][] get_OldParkSPIP(){ // 2 ПСП одинаковой длины - model 1
        Make_OldParkSPIP();
        return psp;
    }

    public int[][] get_SPIP(){ // 2 ПСП одинаковой длины + дополнительные биты инф.- model 2
        Make_SPIP();
        return psp;
    }

    public int[][] get_1384SPIP(){ // 2 ПСП одинаковой длины + дополнительная последовательность - model 3
        Make_1384SPIP();
        return psp;
    }

    public ArrayList<Integer> getSP() { return this.SP; }
    public ArrayList<Integer> getIP() { return this.IP; }
    public ArrayList<Integer> getDP() { return this.DP; }

    //-----------------------------------------
    // private
    //-----------------------------------------
    private void MakeSPIP(int[] reg_length, int[] feedback, int[] start, int[] psp_length){
        for (int i = 0; i < 3; i++) {
            gen_psp[i] = new MainPSP(reg_length[i], feedback[i], start[i], psp_length[i]);
        }
    }

    private void Make_OldParkSPIP(){ // model 1
        psp = new int[2][Size[0]];
        SP = new ArrayList<Integer>();
        IP = new ArrayList<Integer>();
        for (int i = 0; i < Size[0]; i++) {
            psp[0][i] = (gen_psp[0].getPSP(i) ^ gen_psp[1].getPSP(i)) == 1 ? 1 : -1;
            psp[1][i] = (gen_psp[1].getPSP(i) ^ gen_psp[2].getPSP(i)) == 1 ? 1 : -1;
            SP.add(psp[0][i]);
            IP.add(psp[1][i]);
        }
    }

    private void MakeNewParkSPIP(){
        psp = new int[2][Size[0]];
        SP = new ArrayList<Integer>();
        IP = new ArrayList<Integer>();
        for (int i = 0; i < Size[0]; i++) {
            psp[0][i] = (gen_psp[0].getPSP(i) ^ gen_psp[1].getPSP(i)) == 1 ? 1 : -1;
            psp[1][i] = (gen_psp[1].getPSP(i) ^ gen_psp[2].getPSP(i)) == 1 ? 1 : -1;
            SP.add(psp[0][i]);
            IP.add(psp[1][i]);
        }
    }

    private void Make_SPIP(){ // model 2
        psp = new int[2][Size[0]];
        SP = new ArrayList<Integer>();
        IP = new ArrayList<Integer>();
        for (int i = 0; i < Size[0]; i++) {
            psp[0][i] = (gen_psp[0].getPSP(i)) == 1 ? 1 : -1;
            psp[1][i] = (gen_psp[1].getPSP(i)) == 1 ? 1 : -1;
            SP.add(psp[0][i]);
            IP.add(psp[1][i]);
        }
    }

    private void Make_1384SPIP(){ // model 3
        psp = new int[2][Size[0]];
        SP = new ArrayList<Integer>();
        IP = new ArrayList<Integer>();
        for (int i = 0; i < Size[0]; i++) {
            psp[0][i] = (gen_psp[0].getPSP(i)) == 1 ? 1 : -1;
            psp[1][i] = (gen_psp[1].getPSP(i)) == 1 ? 1 : -1;
            //psp[2][i] = (gen_psp[2].getPSP(i)) == 1 ? 1 : -1;
            SP.add(psp[0][i]);
            IP.add(psp[1][i]);
            //DP.add(psp[2][i]);
        }
    }
}
