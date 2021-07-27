

public class MainSignals {

    //----------------------------------------
    public static class TQuads {
        private double Re, Im;

        public TQuads(double re, double im) {
            this.Re = re;
            this.Im = im;
        }

        public void setRe(double re) {
            this.Re = re;
        }

        public void setIm(double im) {
            this.Im = im;
        }

        public double getRe() {
            return Re;
        }

        public double getIm() {
            return Im;
        }

        public void Clear() {
            Re = 0;
            Im = 0;
        }

        public void Add(TQuads value) {
            Re = Re + value.Re;
            Im = Im + value.Im;
        }

        public void Mul(double value) {
            Re = Re * value;
            Im = Im * value;
        }

        public double GetMax() {
            return Math.max(Math.abs(this.Re), Math.abs(this.Im));
        }

        public void Rotate(double phase, double step) {
            double si, co, rr, ii;
            si = Math.sin(phase);
            co = Math.cos(phase);
            rr = Re * co - Im * si;
            ii = Re * si + Im * co;
            Re = rr;
            Im = ii;

            phase += step;

            if (phase > Math.PI * 2)                                 // Чтобы не терять точность
                phase -= Math.PI * 2;
            else if (phase < -Math.PI * 2)
                phase += Math.PI * 2;
        }
    }
    //----------------------------------------
    public static class TSSGaussNoise {
        private double Ampl;

        public TSSGaussNoise(double aAmpl) {
            this.Ampl = aAmpl;
        }

        public String AsString() {
            return "Нормальный белый шум A=" + String.format("%1.2f", Ampl);
        }

        public double GetSignal() {
            double d = 0.0;
            for (int i = 1; i <= 12; i++)// Нормализация до дисперсии 1
                d += (Math.random() - 0.5);
            return d * Ampl;
        }

        public TQuads GetQuads() {
            TQuads q = new TQuads(GetSignal() / Math.sqrt(2), GetSignal() / Math.sqrt(2));
            return q;
        }
    }
    //----------------------------------------
    public static class TSignalSourse implements ISignalSource {
        private double FAmpl, FFD;
        private double FFreq, FPhase, FStep;
        private String OutString;

        public TSignalSourse () {}
        //-----------------------------------------
        @Override
        public TSignalSourse CreateSignal(Object ampl, Object ffd, Object freq) {
            this.FAmpl = (double) ampl;
            this.FFD  = (double) ffd;
            this.FFreq = (double) freq;
            this.FPhase = Math.random() * Math.PI * 2;
            this.FStep =  Math.PI * 2 * FFreq / FFD;
            return this;
        }
        @Override
        public double GetSignal(int spi, int ipi) {
            double d =  (ipi * Math.sin (FPhase) + spi * Math.cos (FPhase)) * FAmpl / Math.sqrt(2.0);
            FPhase += FStep;
            return d;
        }
        @Override
        public TQuads GetQuads() {
            TQuads q = new TQuads(GetSignal(0,0), GetSignal(0,0));
            return q;
        }
        @Override
        public String asString() {
            return this.OutString;
        }
        @Override
        public double GetAmpl() {
            return this.FAmpl;
        }
        @Override
        public void SetAmpl(double ampl) {
            this.FAmpl = ampl;
        }
        public void SetOutString (String s){ this.OutString = s; }

    //-----------------------------------------
    // private
    //-----------------------------------------
        private TQuads DoQuads(double MxRe, double MxIm) {
            TQuads q = new TQuads(0.0, 0.0);
            q.Re = FAmpl * MxRe;
            q.Im = FAmpl * MxIm;
            q.Rotate (FPhase, FStep);
            return q;
        }
    }
    //----------------------------------------
    public static class TQPSK {
        TSignalSourse signal;
        private double FPhaseC, FPhaseP, FStepC, FDF, FFreq;
        private int FFPC, FShPSP;
        private Constants.TSigMode FSigMode;

        public MainRD FRD;
        double FDFN, FDFP, FStepP;
        int SPCount;
        int[][] spip;

        public TQPSK (TSignalSourse s, MainRD rd) {
            this.signal  = s;
            FRD = rd;
            /*FShPSP  = shPSP;
            FDF = 0; //DeltaF;
            FFPC    = 0; //FPC;
            FPhaseC = Math.random() * Math.PI * 2;
            FPhaseP = 0;
            Reset ();*/
        }

        public double getSignal (int sp, int ip) {
            return signal.GetSignal(sp, ip);
        }
        //-----------------------------------------
        // private
        //-----------------------------------------
        private void Reset() {
            /*FFreq = FFPC + FRD.FCarry * FDF; //        ПЧ
            FStepC = 2 * Math.PI * FFreq / FDF;
            FStepP = FRD.FTakt * (1 + FDF) / signal.FFD;*/
        }
    }
    //----------------------------------------------

}

/*uses
  SysUtils, Math, MyRD, MyFIR;
const
  Pi2 = Pi * 2;

type
  TGetInfoBit = function (NumOf50: integer): integer of object;

  TSSImpulse = class (TSignalSource)
  private
    FBusy, FCount, FTau: integer;
    FPlus: boolean;
    function  _Get_Busy: integer;
    procedure _Set_Busy (Value: integer);
  public
    constructor Create (aAmpl, aBusy: integer);
    function  AsString:  string;                                       override;
    function  GetSignal: double;                                       override;
    function  GetQuads:  TQuads;                                       override;
    property  Busy: integer read _Get_Busy write _Set_Busy;
  end;

  TSSSin = class (TSignalSource)
  public
    constructor Create (Ampl, FDiskr, Freq: double);
    procedure SetFreq  (Freq: double);
    function  AsString:  string;                                       override;
    function  GetSignal: double;                                       override;
    function  GetQuads:  TQuads;                                       override;
  end;

  TSSUPP = class (TSSSin)
  private
    FFIR: array [0..1] of TMyFIR;
    function DoSignal (n: integer = 0): double;
  public
    constructor Create (Ampl, FDiskr, Freq: double);
    destructor  Destroy;                                               override;
    procedure FIRLoadFromFile (FileName: string);
    procedure FIRLoadFromRes  (ResName:  string);
    function  AsString:  string;                                       override;
    function  GetSignal: double;                                       override;
    function  GetQuads:  TQuads;                                       override;
  end;

  TSigMode = (smZeroF, smHighF, smMidF);

  TSSQPSK = class (TSignalSource)
  private
    FPhaseP, FIPAngle, FIPAmpl: double;
    FShPSP, FLastSP: integer;
    FSigMode: TSigMode;
    function  _Get_Ampl: double;                                       override;
    procedure _Set_Ampl (Value: double);                               override;
    procedure GetSPIP (var spi, ipi: integer);
  public
    FRD: TRD;
    FMan: boolean;
    FDFN, FDFP, FStepP: double;
    SPCount: integer;
    GetInfoBit: TGetInfoBit;
    constructor Create (aAmpl, FDiskr: double;
                        ShPSP: integer; SigMode: TSigMode);            overload;
    constructor Create (RD: TRD; aAmpl, FDiskr: double; Man: boolean;
                        ShPSP: integer; SigMode: TSigMode);            overload;
    procedure Reset;
    procedure UpdateF;
    procedure SetRD     (RD: TRD);
    procedure SetDeltaF (dFN, dFP: double);
    procedure SetShPSP  (Value: integer);
    procedure SetFDiskr (FD: double);
    procedure SetIPAngle (Degree: integer);       // Погрешности ИП: Ошибка угла
    procedure SetIPAmpl  (Percents: integer);     //                 Апилитуда
    function  Freq:      double;
    function  AsString:  string;                                       override;
    function  GetSignal: double;                                       override;
    function  GetQuads:  TQuads;                                       override;
  end;

//------------------------------------------------------------------------------

{ TSSImpulse }

//------------------------------------------------------------------------------
constructor TSSImpulse.Create (aAmpl, aBusy: integer);
begin
  inherited Create (aAmpl, 0);

  Busy   := aBusy;
  FCount := 0;
end;
//------------------------------------------------------------------------------
function TSSImpulse._Get_Busy: integer;
begin
  Result := FBusy;
end;
//------------------------------------------------------------------------------
procedure TSSImpulse._Set_Busy (Value: integer);
begin
  FBusy := Value;                                   // Скважность в 1000-х долях
end;
//------------------------------------------------------------------------------
function TSSImpulse.AsString: string;
begin
  Result := 'Импульсная помеха A=' + FormatFloat (',0.00', FAmpl) +
             ', Q =' + FormatFloat (',0.00', FBusy / 1000);
end;
//------------------------------------------------------------------------------
function TSSImpulse.GetSignal: double;
begin
  if FCount = 0 then
    begin
      FTau  := 500 + Random (1000);
      FPlus := not FPlus;
    end;

  Result := IfThen (FCount < FBusy, IfThen (FPlus, FAmpl, -FAmpl));

  FCount := (FCount + 1) mod FTau;
end;
//------------------------------------------------------------------------------
function TSSImpulse.GetQuads: TQuads;
begin
  Result.Re := GetSignal;
  Result.Im := Result.Re;
end;
//------------------------------------------------------------------------------

{ TSSSin }

//------------------------------------------------------------------------------
constructor TSSSin.Create (Ampl, FDiskr, Freq: double);
begin
  inherited Create (Ampl, FDiskr);

  SetFreq (Freq);
end;
//------------------------------------------------------------------------------
procedure TSSSin.SetFreq (Freq: double);
begin
  FFreq := Freq;
  FStep := FFreq * Pi2 / FFD;
end;
//------------------------------------------------------------------------------
function TSSSin.AsString: string;
begin
  Result := 'Синус F=' + FormatFloat (',0.00', FFreq) +
                ', A=' + FormatFloat (',0.00', FAmpl);
end;
//------------------------------------------------------------------------------
function TSSSin.GetSignal: double;
begin
  Result := FAmpl * Sin (FPhase);
  FPhase := FPhase + FStep;
end;
//------------------------------------------------------------------------------
function TSSSin.GetQuads: TQuads;
begin
  Result := DoQuads (1, 1);
end;
//------------------------------------------------------------------------------

{ TSSUPP }

//------------------------------------------------------------------------------
constructor TSSUPP.Create (Ampl, FDiskr, Freq: double);
begin
  inherited;

  FFIR [0] := TMyFIR.Create;
  FFIR [1] := TMyFIR.Create;
end;
//------------------------------------------------------------------------------
destructor TSSUPP.Destroy;
begin
  FFIR [0].Free;
  FFIR [1].Free;

  inherited;
end;
//------------------------------------------------------------------------------
function TSSUPP.AsString: string;
begin
  Result := 'УПП F=' + FormatFloat (',0.00', FFreq) +
              ', A=' + FormatFloat (',0.00', FAmpl);
end;
//------------------------------------------------------------------------------
function TSSUPP.DoSignal (n: integer = 0): double;
var
  i: integer;
  d: double;
begin
  d := 0;
  for i := 1 to 12 do                             // Нормализация до дисперсии 1
    d := d + (Random - 0.5);
  d := d * FAmpl;

  Result := FFIR [n].Filter (d);
end;
//------------------------------------------------------------------------------
function TSSUPP.GetSignal: double;
begin
  Result := DoSignal;
end;
//------------------------------------------------------------------------------
function TSSUPP.GetQuads: TQuads;
begin
  Result := DoQuads (DoSignal (0), DoSignal (1));
end;
//------------------------------------------------------------------------------
procedure TSSUPP.FIRLoadFromFile (FileName: string);
begin
  FFIR [0].LoadFromFile (FileName);
  FFIR [1].LoadFromFile (FileName);
end;
//------------------------------------------------------------------------------
procedure TSSUPP.FIRLoadFromRes (ResName: string);
begin
  FFIR [0].LoadFromRes (ResName);
  FFIR [1].LoadFromRes (ResName);
end;
//------------------------------------------------------------------------------

{ TSSQPSK }

//------------------------------------------------------------------------------
constructor TSSQPSK.Create (aAmpl, FDiskr: double; ShPSP: integer; SigMode: TSigMode);
begin
  inherited Create (aAmpl, FDiskr);

  FSigMode := SigMode;
  FDFN     := 0;                               // Общая расстройка несущей и ПСП
  FDFP     := 0;                               // Дополнительная расстройка  ПСП
  FShPSP   := ShPSP;
  FMan     := true;
  FPhaseP  := 0;

  SetIPAngle (0);
  SetIPAmpl  (100);

  FRD.FCarry := 0;
  UpdateF;

  GetInfoBit := nil;
end;
//------------------------------------------------------------------------------
constructor TSSQPSK.Create (RD: TRD; aAmpl, FDiskr: double;
                            Man: boolean; ShPSP: integer; SigMode: TSigMode);
begin
  Create (aAmpl, FDiskr, ShPSP, SigMode);

  FRD  := RD;
  FMan := Man;
  UpdateF;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.SetFDiskr (FD: double);
begin
  FFD := FD;
  UpdateF;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.UpdateF;
var
  df: double;
begin
  df := FRD.FCarry * FDFN;

  case FSigMode of
    smZeroF: FFreq := df;
    smHighF: FFreq := df + FRD.FCarry;
    smMidF:  FFreq := df + FRD.FCarry - 33.8E6;
  end;

  FStep  := -FFreq * Pi2 / FFD;
  FStepP := FRD.FTakt * (1 + FDFN + FDFP) / FFD;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.Reset;
begin
  UpdateF;
  FLastSP := 0;
  SPCount := 0;
end;
//------------------------------------------------------------------------------
function TSSQPSK._Get_Ampl: double;
begin
  Result := FAmpl * Sqrt (2);
end;
//------------------------------------------------------------------------------
procedure TSSQPSK._Set_Ampl (Value: double);
begin
  FAmpl := Value / Sqrt (2);
end;
//------------------------------------------------------------------------------
function TSSQPSK.Freq: double;
begin
  Result := FFreq;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.SetDeltaF (dFN, dFP: double);
begin
  FDFN := dFN;
  FDFP := dFP;
  UpdateF;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.SetShPSP (Value: integer);
begin
  FShPSP := Value;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.SetRD (RD: TRD);
begin
  FRD := RD;
  UpdateF;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.SetIPAngle (Degree: integer);
begin
  FIPAngle := Pi * Degree / 180;
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.SetIPAmpl (Percents: integer);
begin
  FIPAmpl := Percents / 100;
end;
//------------------------------------------------------------------------------
function TSSQPSK.AsString: string;
begin
  Result := 'ФМ ШПС ' + FRD.AsString + ' F=' + FormatFloat (',0.00', FFreq) +
                                      ', A=' + FormatFloat (',0.00', Ampl)  +
                                      ', C=' + IntToStr (FShPSP);
end;
//------------------------------------------------------------------------------
procedure TSSQPSK.GetSPIP (var spi, ipi: integer);
var
  i: integer;
begin
  with FRD do
    begin
      i   := (Round (FPhaseP) + FShPSP) mod Length (SP);
      spi := SP [i];
      if i < FLastSP then                 // Начался новый период СП - сосчитаем
        Inc (SPCount);
      FLastSP := i;
      i   := (Round (FPhaseP) + FShPSP) mod Length (IP);
      ipi := IP [i];

      i := (Round (FPhaseP) + FShPSP) div Length (IPs [0]);          // Номер ИП
      if Assigned (GetInfoBit)
        then i := GetInfoBit (i mod 50) // Функция есть - спросим бит информации
        else i := i mod 3;              // По умолчанию - косой меандр

      if i = 0 then                                   // А вот принимается 1 ???
        ipi := -ipi;

      FPhaseP := FPhaseP + FStepP;                   // Чтобы не терять точность
      i := Length (IP) * 3;
      if FPhaseP > i then
        FPhaseP := FPhaseP - i;
    end;

  if not FMan then
    begin
      spi := 1;
      ipi := 1;
    end;
end;
//------------------------------------------------------------------------------
function TSSQPSK.GetSignal: double;
var
  spi, ipi: integer;
begin
  GetSPIP (spi, ipi);

  Result := (ipi * FIPAmpl * Sin (FPhase + FIPAngle) + spi * Cos (FPhase)) * FAmpl;

  FPhase := FPhase + FStep;

  if FPhase > Pi2 then                               // Чтобы не терять точность
    FPhase := FPhase - Pi2;
end;
//------------------------------------------------------------------------------
function TSSQPSK.GetQuads: TQuads;
var
  spi, ipi: integer;
  x, y: double;
begin
  GetSPIP (spi, ipi);

  x := ipi * FIPAmpl * Sin (FIPAngle) + spi;
  y := ipi * FIPAmpl * Cos (FIPAngle);

  Result := DoQuads (x, y);
end;
//------------------------------------------------------------------------------

*/

