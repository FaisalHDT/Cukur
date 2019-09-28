package com.example.cukur.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cukur.Adapter.HomeSliderAdapter;
import com.example.cukur.Adapter.LookBookAdapter;
import com.example.cukur.BookingActivity;
import com.example.cukur.Common.Common;
import com.example.cukur.Interface.IBannerLoadListener;
import com.example.cukur.Interface.ILookbookLoadlistner;
import com.example.cukur.Model.Banner;
import com.example.cukur.R;
import com.example.cukur.Services.PicassoLoadingService;
import com.facebook.accountkit.AccountKit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ILookbookLoadlistner, IBannerLoadListener {

    private Unbinder unbinder;

    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;
    @BindView(R.id.txt_user_name)
    TextView txt_user_name;
    @BindView(R.id.banner_slider)
    Slider banner_slider;
    @BindView(R.id.recycler_look_book)
    RecyclerView recycler_look_book;
    @OnClick (R.id.card_view_booking)
    void booking ()
    {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    //Firesetore
    CollectionReference bannerRef, lookbookRef;

    //interface
    IBannerLoadListener iBannerLoadListener;
    ILookbookLoadlistner iLookbookLoadlistner;

    public HomeFragment() {
        // Required empty public constructor
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookbookRef = FirebaseFirestore.getInstance().collection("Lookbook");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder= ButterKnife.bind(this, view);

        // init
        //Slider.init(Slider.getImageLoadingService());
        Slider.init(new PicassoLoadingService());
        iBannerLoadListener =this;
        iLookbookLoadlistner=this;

        //check is logged
        if (AccountKit.getCurrentAccessToken()!= null)
        {
            setUserInformation();
            loadbanner();
            loadlookbook();
        }
        return view;
    }

    private void loadlookbook() {
        lookbookRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> lookbooks = new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot bannerSnapShot:task.getResult())
                            {
                                Banner banner =bannerSnapShot.toObject(Banner.class);
                                lookbooks.add(banner);
                            }
                            iLookbookLoadlistner.onLookbookLoadSuccess(lookbooks);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookbookLoadlistner.onLookbookLoadFailed(e.getMessage());
            }
        });
    }

    private void loadbanner() {
        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners = new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot bannerSnapShot:task.getResult())
                            {
                                Banner banner =bannerSnapShot.toObject(Banner.class);
                                banners.add(banner);
                            }
                            iBannerLoadListener.onBannerLoadSuccess(banners);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
    }

    @Override
    public void onLookbookLoadSuccess(List<Banner> banners) {
        recycler_look_book.setHasFixedSize(true);
        recycler_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_look_book.setAdapter(new LookBookAdapter(getActivity(),banners));
    }

    @Override
    public void onLookbookLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {

        banner_slider.setAdapter(new HomeSliderAdapter(banners));

    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
