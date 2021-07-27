/*
 Класс для реализации ПСП
*/

import java.util.ArrayList;
import java.util.Collections;

// 10-64-1-1007
// 10-64-1007-1007
// 16-14335-65535-50350
// createPSP (10,64,1,1007);
public class MainPSP {

    private int FReg_Length = 0;
    private int FFeedBack = 0;
    private int FStart = 0;
    private int FPSP_length = 0;
    private ArrayList <Integer> FPSP = new ArrayList<Integer>();

    private int FReg;
    private int FCnt;
    private int FOutMask;

    public MainPSP () {
        setRegLength (0);                                       // Для начала
    }

    public MainPSP (int reg_length, int feedback, int start, int psp_length) {
        createPSP(reg_length, feedback, start, psp_length);
    }

    public void createPSP (int reg_length, int feedback, int start, int psp_length) {
        this.FReg_Length = reg_length;
        this.FFeedBack = feedback;
        this.FStart = start;
        this.FPSP_length = psp_length;

        setFeedBack  (FFeedBack);
        setStart     (start);
        setRegLength (FReg_Length);                               // В последнюю очередь
        FPSP_length = psp_length;
        Update (); // сосчитать ПСП
    }
    public int getFeedBack() { return this.FFeedBack; }
    public int getRegLength () { return this.FReg_Length; }
    public int getPSP (int index) {
        int psp = Math.floorMod (index, FPSP_length);
        return FPSP.get(psp);
    }
    public int getStart () { return this.FStart; }
    //------------------------------------------
    public int Next () {
        FCnt = Math.floorMod(FCnt, FPSP_length);
        if (FCnt == 0)
            Reset();
        FCnt++;

        int next = (FReg & FOutMask) == 0 ? 0 : 1;
        int tmp  = (FReg ^ (next != 0 ? FFeedBack : 0 )) & (FOutMask - 1);
        FReg = (tmp << 1) | next;
        return next;
    }
    //------------------------------------------
    public ArrayList<String> PSP_asList () {
        ArrayList<String> list = new ArrayList<>();
        String psp = "";
        for (int i : FPSP)
            psp = psp.concat(String.valueOf(i));
        list.add(psp);
        return list;
    }
    //------------------------------------------
    public ArrayList <Integer> currentFPSP () {
        return this.FPSP;
    }
    //------------------------------------------
    public ArrayList <Integer> currentFPSP_revers () {
        ArrayList<Integer> newPsp = new ArrayList<>(this.FPSP);
        Collections.reverse(newPsp);
        return newPsp;
    }

    //----------------------------------
    // private
    //----------------------------------
    private void setRegLength(int value) {
        FPSP_length = 1;
        FPSP.clear();
        FPSP.add(0);
        FOutMask = 0;
        if (value == 0)
            return;

        if (value < 3 || value > 16)
            //Exception.Create ('ПСП: Недопустимая длина регистра');
            return;

        FReg_Length = value;
        setPSP_length((1 << FReg_Length) - 1);
        FOutMask = 1 << (FReg_Length - 1);
        Update();
    }
    //------------------------------------------
    private void setFeedBack(int fb) {
        this.FFeedBack = fb;
        Update();
    }
    //------------------------------------------
    private void setPSP_length(int length) {
        if ((length <= 0) || (length > FPSP.size())) // Ничего не меняем
            return;
        this.FPSP_length = length;
    }
    public int getPSP_length() { return this.FPSP_length; }
    //------------------------------------------
    private void setStart(int start) {
        this.FStart = start;
        Update();
    }
    //------------------------------------------
    private void Reset() {
        FCnt = 0;
        FReg = FStart;
    }
    //------------------------------------------
    private void Update () {
        Reset ();
        FPSP.clear();
        for (int i = 0; i < FPSP_length; i++)
            FPSP.add (i, Next());
    }

}