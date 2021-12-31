package com.example.android.boardingpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.android.boardingpass.databinding.ActivityMainBinding;
import com.example.android.boardingpass.utitilites.FakeDataUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        BoardingPassInfo fakeBoardingPassInfo = FakeDataUtils.generateFakeBoardingPassInfo();

        displayBoardingPassInfo(fakeBoardingPassInfo);

    }

    private void displayBoardingPassInfo(BoardingPassInfo info) {

        mBinding.textViewPassengerName.setText(info.passengerName);
        mBinding.flightInfo.textViewOriginAirport.setText(info.originCode);
        mBinding.flightInfo.textViewFlightCode.setText(info.flightCode);
        mBinding.flightInfo.textViewDestinationAirport.setText(info.destCode);

        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault());

        String boardingTime = formatter.format(info.boardingTime);
        String departureTime = formatter.format(info.departureTime);
        String arrivalTime = formatter.format(info.arrivalTime);
        mBinding.textViewBoardingTime.setText(boardingTime);
        mBinding.textViewDepartureTime.setText(departureTime);
        mBinding.textViewArrivalTime.setText(arrivalTime);

        long totalMinutesUntilBoarding = info.getMinutesUntilBoarding();
        long hoursUnitBoarding = TimeUnit.MINUTES.toHours(totalMinutesUntilBoarding);
        long minutesLessHoursUntilBoarding =
                totalMinutesUntilBoarding - TimeUnit.HOURS.toMinutes(hoursUnitBoarding);

        String hoursAndMinutesUntilBoarding = getString(R.string.countDownFormat, hoursUnitBoarding, minutesLessHoursUntilBoarding);
        mBinding.textViewBoardingInCountdown.setText(hoursAndMinutesUntilBoarding);

        mBinding.boardingInfo.textViewTerminal.setText(info.departureTerminal);
        mBinding.boardingInfo.textViewGate.setText(info.departureGate);
        mBinding.boardingInfo.textViewSeat.setText(info.seatNumber);
    }
}