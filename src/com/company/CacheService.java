package com.company;
/*

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import net.infonama.mobinfo.apiservice.Oracle;
import net.infonama.mobinfo.data.JsonModel;
import net.infonama.mobinfo.data.MakerModel;
import net.infonama.mobinfo.database.BrandInstance;
import net.infonama.mobinfo.database.UpdateInstance;
import net.infonama.mobinfo.instance.Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class CacheService extends Service {
    public final static String CACHE_SERVICE_RESPONSE = "CACHE_SERVICE_RESPONSE";
    public final static String CACHE_SERVICE_RESPONSE_EXTRA = "CACHE_SERVICE_RESPONSE_EXTRA";

    public static final String RESPONSE_DONE = "RESPONSE_DONE";
    public static final String RESPONSE_ERROR = "RESPONSE_ERROR";
    public static final String RESPONSE_CONNECTION = "RESPONSE_CONNECTION";

    public static final String PROGRESS = "PROGRESS";
    public static final String PROGRESS_MAX = "PROGRESS_MAX";
    public static final String PROGRESS_CURRENT = "PROGRESS_CURRENT";

    private BrandInstance brandInstance;
    private UpdateInstance updateInstance;
    private int all;
    private int size;
    private int current;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.brandInstance = new BrandInstance();
        this.updateInstance = new UpdateInstance();
        updateInstance.empty(this);
        setSize();

        updateCheck(new Oracle.OnFinishListener() {
            @Override
            public void onSuccess(Object response) {
                Log.d("onSuccess", "onSuccess() returned: " + "onSuccess");
                saveData(response);
                setBroad(RESPONSE_DONE);
                stopSelf();
            }

            @Override
            public void internetConnection() {
                setBroad(RESPONSE_CONNECTION);
                stopSelf();
            }

            @Override
            public void onError() {
                setBroad(RESPONSE_ERROR);
                stopSelf();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void setSize() {
        if (Data.getInstance().getJsonList() != null) {
            size = Data.getInstance().getJsonList().size() + 5;
        } else {
            size = 15000;
        }
    }

    private void saveData(final Object response) {
        if (response == null) {
            UpdateInstance instance = new UpdateInstance();
            if (Data.getInstance().getJsonList() == null) {
                ArrayList<JsonModel> myList = getJson();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ArrayList<JsonModel> jsonModelArrayList = instance.getUpdates(CacheService.this);
                    if(myList != null && jsonModelArrayList != null) {
                        myList.addAll(jsonModelArrayList);

                        objectMapper.writeValue(new File(getFilesDir(), JsonModel.LOCATION), myList);
                        instance.empty(CacheService.this);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Data.getInstance().getJsonList().addAll(instance.getUpdates(CacheService.this));

                    objectMapper.writeValue(new File(getFilesDir(), JsonModel.LOCATION), Data.getInstance().getJsonList());
                    instance.empty(CacheService.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setBroad(String action) {
        Intent intent = new Intent(CACHE_SERVICE_RESPONSE);
        intent.putExtra(CACHE_SERVICE_RESPONSE_EXTRA, action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void setProgress(int max_PROGRESS, int current_PROGRESS) {
        Intent intent = new Intent(CACHE_SERVICE_RESPONSE);
        intent.putExtra(CACHE_SERVICE_RESPONSE_EXTRA, PROGRESS);
        intent.putExtra(PROGRESS_CURRENT, current_PROGRESS);
        intent.putExtra(PROGRESS_MAX, max_PROGRESS);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ArrayList<JsonModel> getJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonModel[] models = objectMapper.readValue(new File(getFilesDir(), JsonModel.LOCATION)
                    , JsonModel[].class);
            return new ArrayList<JsonModel>(Arrays.asList(models));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateCheck(final Oracle.OnFinishListener onFinishListener) {
        if (!isNetworkAvailable()) {
            onFinishListener.internetConnection();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://gsmarena.com/makers.php3", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Document htmlDocument = Jsoup.parse(new String(responseBody), "http://gsmarena.com/");
                Elements links = htmlDocument.select("div[class=st-text]");
                if (links == null) {
                    onFinishListener.onError();
                    return;
                }

                ArrayList<MakerModel> list = brandInstance.getBrands(CacheService.this);
                boolean isEnd = true;

                for (int h = 1; h < links.select("td").size(); h += 2) {

                    String address = links.select("a").get(h).absUrl("href");
                    String image_address = links.select("td").get(h - 1).select("img").get(0).absUrl("src");

                    String tag = links.select("td").get(h).text();
                    tag = tag.replace(" phones ", "");

                    String name = tag.substring(0, tag.indexOf("("));
                    String number = tag.substring(tag.indexOf("("));
                    number = number.replace("(", "").replace(")", "");

                    boolean flag = false;
                    int current_number = Integer.parseInt(number);

                    for (MakerModel model : list) {
                        if (address.equalsIgnoreCase(model.getAddress().replace("www.", ""))) {
                            int differ = current_number - model.getNumber();
                            if (differ > 0) {
                                isEnd = false;
                                model.setNumber(current_number);
                                getModels(model, differ, onFinishListener);
                            }
                            list.remove(model);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        isEnd = false;
                        MakerModel newModel = createMaker(current_number, address, image_address, name);
                        getModels(newModel, current_number, onFinishListener);
                    }
                }
                if (isEnd) {
                    onFinishListener.onSuccess("");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                onFinishListener.onError();
            }
        });
    }

    private MakerModel createMaker(int current, String address, String image_address, String name) {
        MakerModel newModel = new MakerModel();
        if (!address.contains("www.")) {
            newModel.setAddress(address.replace("http://", "http://www."));
        } else {
            newModel.setAddress(address);
        }
        newModel.setNumber(Integer.parseInt(String.valueOf(current)));
        newModel.setName(name);
        newModel.setImage_address(image_address);
        ArrayList<MakerModel> oldList = brandInstance.getBrands(CacheService.this);
        oldList.add(newModel);
        Gson gson = new Gson();
        brandInstance.setBrands(gson.toJson(oldList), CacheService.this);
        return newModel;
    }

    private void updateMaker(final MakerModel model) {
        brandInstance.updateModel(model, CacheService.this);
    }

    private void getModels(final MakerModel model, final int number, final Oracle.OnFinishListener onFinishListener) {
        all++;
        String finalAddress = model.getAddress().replace("http://www", "http://m");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(finalAddress, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<JsonModel> arrayList = new ArrayList<>();
                Document htmlDocument = Jsoup.parse(new String(responseBody), "http://m.gsmarena.com/");
                Elements links = htmlDocument.select("div[class=general-menu]");

                for (int h = 0; h < links.select("a").size(); h++) {
                    JsonModel newJson = new JsonModel();
                    String name = links.select("a").get(h).text();
                    String url = links.select("a").get(h).absUrl("href");
                    String image_url = links.select("a").get(h).select("img").get(0).absUrl("src");

                    newJson.setId(size++);
                    newJson.setMaker(model.getName());
                    newJson.setModel(name);
                    newJson.setAddress(url);
                    newJson.setImage_ADDRESS(image_url);

                    arrayList.add(h, newJson);
                    if (h > (number - 1)) {
                        break;
                    }
                }
                setProgress(all, current);

                updateInstance.setUpdates(arrayList, CacheService.this);
                updateMaker(model);
                current++;
                if (current >= all) {
                    onFinishListener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                onFinishListener.onError();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
*/
