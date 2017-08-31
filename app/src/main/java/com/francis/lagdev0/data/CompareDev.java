package com.francis.lagdev0.data;

import java.util.Comparator;

/**
 * Created by Francis on 30/08/2017.
 * A custom class that is used to sort the developers and repositories
 */

public class CompareDev {

    public static Comparator<Developer> compareDevAscending = new Comparator<Developer>() {
        @Override
        public int compare(Developer o1, Developer o2) {
            String username1 = o1.getmDevName().toUpperCase();
            String username2 = o2.getmDevName().toUpperCase();

            // sort dev username in ascending order
            return username1.compareTo(username2);
        }
    };

    public static Comparator<Developer> compareDevDescending = new Comparator<Developer>() {
        @Override
        public int compare(Developer o1, Developer o2) {
            String username1 = o1.getmDevName().toUpperCase();
            String username2 = o2.getmDevName().toUpperCase();

            //sort dev username in descending order
            return username2.compareTo(username1);
        }
    };

    public static Comparator<Developer> compareRepoDescending = new Comparator<Developer>() {
        @Override
        public int compare(Developer o1, Developer o2) {
            int star1 = o1.getStarCount();
            int star2 = o2.getStarCount();

            //sort star count in descending order
            return star2 - star1;
        }
    };
}
