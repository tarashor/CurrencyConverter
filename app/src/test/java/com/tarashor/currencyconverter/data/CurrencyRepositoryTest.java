package com.tarashor.currencyconverter.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

public class CurrencyRepositoryTest {


    private final static String currency = "EUR";

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    ICurrenciesDataSource mRemoteSearchDataSource;


    CurrenciesRepository mCurrenciesRepository;

//    @Captor
//    private val mCallbackCaptor: ArgumentCaptor<AdvancedAutocompletesResponseHandler.AdvancedAutocompletesLoadedListener>? = null


    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
        mCurrenciesRepository = new CurrenciesRepository(mRemoteSearchDataSource);
    }


    @Test
    public void testGetCurrencies() {
        mCurrenciesRepository.getCurrencies(currency);
        verify(mRemoteSearchDataSource)
                .getCurrencies(ArgumentMatchers.eq(currency));

    }


}
