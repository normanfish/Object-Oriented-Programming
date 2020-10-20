package filesprocessing.FilterPackage;


import java.util.ArrayList;


class NoFilter extends FilterFunc {


    boolean actualFilter(String fileName) {
        return true;
    }


    void update(ArrayList<String> testParameters) {
    }
}
