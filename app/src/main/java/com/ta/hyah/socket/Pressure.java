package com.ta.hyah.socket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Pressure {
    private ArrayList<Float> data;
    private ArrayList<Integer> peaks;
    private ArrayList<Integer> pattern;
    public int systole;
    public int diastole;
    private int heartRate;
    private static final float MAGIC_CONST = 38.23f;
    String TAG = getClass().getSimpleName();

    public Pressure(ArrayList<String> data) {
        ArrayList<Float> dataFloat = new ArrayList<>();
        for (String a : data) {
            dataFloat.add(Float.parseFloat(a));
        }
        this.data = dataFloat;
        this.peaks = new ArrayList<>();
    }

    public Pressure(ArrayList<Float> data, int i) {
        this.data = data;
        this.peaks = new ArrayList<>();
    }

    private void findPeaks() {
        float prev = 0;
        boolean up = false;
        for (int i = 0; i < data.size(); ++i) {
            if (data.get(i) > prev) {
                up = true;
            } else if (up) {
                peaks.add(i - 1);
                up = false;
            }
            prev = data.get(i);
        }
    }

    private void getHeartBeatPattern() {
        for (int i = 0; i < peaks.size(); ++i) {
            //System.out.println(data.get(peaks.get(i)));
        }
        ArrayList<Integer> peakPattern;
        int max = 0;
        boolean shouldContinue;

        for (int k = 0; k < peaks.size() - 2 - k; ++k) {
            for (int i = k + 1; i < peaks.size() - k; ++i) {
                shouldContinue = false;
                peakPattern = new ArrayList<>(peaks);
                for (int l = 0; l < k; ++l) {
                    peakPattern.remove(0);
                }
                int dif = Math.abs(peakPattern.get(k) - peakPattern.get(i));
                int prev = peakPattern.get(i);
                for (int j = i + 1; j < peakPattern.size(); ++j) {
                    if (Math.abs(prev - peakPattern.get(j)) < dif + 5) {
                        peakPattern.remove(j);
                        --j;
                    } else if (Math.abs(prev - peakPattern.get(j)) > dif + 5) {
                        shouldContinue = true;
                        for (int m = j; m < peakPattern.size(); ++m) {
                            peakPattern.remove(m);
                            --m;
                        }
                        break;
                    } else {
                        prev = peakPattern.get(j);
                    }
                }

                if (max < peakPattern.size()) {
                    pattern = new ArrayList<>(peakPattern);
                    max = pattern.size();
                }

                if (shouldContinue) {
                    break;
                }

                //System.out.println(i + " " + peakPattern.size());

                if (max < peakPattern.size()) {
                    pattern = new ArrayList<>(peakPattern);
                    max = pattern.size();
                }
            }
        }
    }

    public String getPressure() {
        prepare();
        result();
        return systole + "/" + diastole;
    }

    private void smoothen() {
        float prev = data.get(0);
        for (int i = 1; i < data.size(); ++i) {
            if (Math.abs(data.get(i) - prev) >= 8) {
                data.remove(i);
                --i;
            } else {
                prev = data.get(i);
            }
        }
    }

    private void prepare() {
        smoothen();
        findPeaks();
        getHeartBeatPattern();
    }

    private void result() {
        systole = (int) (data.get(pattern.get(0)) + MAGIC_CONST);
        diastole = (int) (data.get(pattern.get(pattern.size() - 1)) + MAGIC_CONST);
    }

    public void ahay(ArrayList data) {
        Pressure p = new Pressure(data);
    }

    public String getHeartRate() {
        for (int i = 1; i < pattern.size()-1; ++i) {
            heartRate += (int) ((6000 / Math.abs(pattern.get(i) - pattern.get(i+1)) * 0.05));
        }
        heartRate /= pattern.size()-2;
        return heartRate + "";
    }

    /*public static void main(String[] args) {
        FileReader fr;
        BufferedReader br;
        String a;
        ArrayList<String> dat = new ArrayList<>();
        try {
            fr = new FileReader("C:\\Users\\Andrew Cen\\Desktop\\Haaa\\src\\data.txt");
            br = new BufferedReader(fr);
            while ((a = br.readLine()) != null) {
                dat.add(a);
            }
            fr.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
