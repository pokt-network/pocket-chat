package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.activity_screen.MessageScreenActivity;
import com.example.myapplication.models.Message;

import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.core.errors.PocketError;
import network.pocket.eth.exceptions.EthContractException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

}