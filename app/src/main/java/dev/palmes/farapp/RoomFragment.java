package dev.palmes.farapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.palmes.farapp.models.Room;

public class RoomFragment extends Fragment {
    private static final String DESCRIBABLE_KEY = "room";
    private Room room;

    public static RoomFragment newInstance(Room describable) {
        RoomFragment fragment = new RoomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        room = (Room) getArguments().getSerializable(DESCRIBABLE_KEY);

        View view = inflater.inflate(R.layout.fragment_room, container, false);

        TextView capacityView = view.findViewById(R.id.roomCapacity);
        if (room.getCapacity().isPresent()) {
            capacityView.setText(room.getCapacity().get() + " people");
            capacityView.setVisibility(TextView.VISIBLE);
        }

        ((TextView) view.findViewById(R.id.roomName)).setText(room.getName());
        ((TextView) view.findViewById(R.id.roomCode)).setText(room.getCode());
        ((TextView) view.findViewById(R.id.roomFloor)).setText("Floor " + room.getFloor());
        ((TextView) view.findViewById(R.id.roomAvailable)).setText(room.isAvailable() ? "Available" : "Occupied");
        ((TextView) view.findViewById(R.id.roomAvailable)).setTextColor(room.isAvailable() ? getResources().getColor(R.color.cylime, getContext().getTheme()) : getResources().getColor(R.color.red, getContext().getTheme()));
        ((TextView) view.findViewById(R.id.roomAvailableUntil)).setText(room.getUntilString());


        // Inflate the layout for this fragment
        return view;
    }
}