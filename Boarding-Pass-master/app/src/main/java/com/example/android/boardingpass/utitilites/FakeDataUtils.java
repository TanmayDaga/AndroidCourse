package com.example.android.boardingpass.utitilites;

import com.example.android.boardingpass.BoardingPassInfo;
import com.example.android.boardingpass.R;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class FakeDataUtils {


    public static BoardingPassInfo generateFakeBoardingPassInfo(){

        BoardingPassInfo bpi = new BoardingPassInfo();
        bpi.passengerName = "MR. TANMAY DAGA";
        bpi.flightCode = "UD 777";
        bpi.originCode = "JFK";
        bpi.destCode = "DCA";

        long now = System.currentTimeMillis();
        long randomMinutesUntilBoarding = (long) (Math.random() *30);
        long totalBoardingMinutes = 40;
        long randomFlightLengthHours = (long) (Math.random() *5 +1);

        long boardingMillis = now + minutesToMillis(randomMinutesUntilBoarding);
        long departure = boardingMillis + minutesToMillis(totalBoardingMinutes);
        long arrival = departure + hoursToMillis(randomFlightLengthHours);

        bpi.boardingTime = new Timestamp(boardingMillis);
        bpi.departureTime = new Timestamp(departure);
        bpi.arrivalTime = new Timestamp(arrival);

        bpi.departureTerminal = "3A";
        bpi.departureGate = "33C";
        bpi.seatNumber = "1A";

        bpi.barCodeImageResource = R.mipmap.barcode;
        return bpi;







    }

    private static long minutesToMillis(long minutes){
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    private static long hoursToMillis(long hours){
        return TimeUnit.HOURS.toMillis(hours);
    }
}
