package com.example.laptop.finalproject;


import android.content.Context;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.presenters.MainPresenter;
import com.example.laptop.finalproject.services.IRestaurantsAPI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;

public class MainPresenterTest {

    @Mock
    @Inject
    IRestaurantsAPI iRestaurantsAPI;

    @Mock
    MainContract.IMainView iMainView;

    @Mock
    MainInteracter interacter;

    @InjectMocks
    MainPresenter presenter;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {

        iMainView = mock(MainContract.IMainView.class);

        presenter = new MainPresenter(interacter);

    }

    @After
    public void tearDown() {

    }

    //test if user input verification works as intended
    @Test
    public void testPresenterUserInputVerificationCorrect() {

        presenter.bind(iMainView);

        presenter.getUserInputs(context, Constants.USE_MY_LOCATION, "", "", "", "");

        Mockito.verify(iMainView).confirmData(true);
    }

    //test if the verification catches invalid user input
    @Test
    public void testPresenterUserInputVerificationIncorrect() {

        presenter.bind(iMainView);

        presenter.getUserInputs(context, "", "", "", "", "");

        Mockito.verify(iMainView).confirmData(false);
    }



}
