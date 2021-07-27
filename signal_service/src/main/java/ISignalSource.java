public interface ISignalSource {
    Object CreateSignal(Object ampl, Object ffd, Object freq);
    double GetSignal(int isp, int iip);
    String asString();
    MainSignals.TQuads GetQuads();
    double GetAmpl();
    void SetAmpl (double ampl);
}