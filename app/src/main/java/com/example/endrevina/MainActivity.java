package com.example.endrevina;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MainActivity extends AppCompatActivity {

    private TextView txtTrack;

    private Chronometer txtTimmer;
    private long Time = 0;

    private Button btGuess;

    private EditText txtFindTheNumber;

    private TextView txtAttempts;
    private int attempts = 0;

    private int randomNumber;

    private Button btAgain;
    private Button btSaveScore;

    private final File filesDir = new File("/data/user/0/com.example.endrevina/files");;
    private final File filePhotoDir = new File("/data/user/0/com.example.endrevina/files/photos");
    private final File fileRanking = new File("/data/user/0/com.example.endrevina/files/ranking.xml");

    private final String extPhoto = ".jpeg";

    private File photoFile;

    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document rankingDoc;
    private DOMImplementation domi;
    private Transformer transformer;

    private ArrayList<User> usersList;
    private User newUser;

    private String nameUser;
    private int attemptsUser;
    private String timeUser;
    private Bitmap photoUser;

    //private long startTime;
    //private long endTime;

    private CharSequence charSequenceText;

    private AlertDialog.Builder adb;
    private AlertDialog adRanking;

    private EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersList = new ArrayList<User>();
        //
        RankingActivity rankingActivity = new RankingActivity();

        randomNumber = 50;
        //randomNumber = (int) (Math.random()*100);
        //System.out.println(randomNumber);

        txtTrack = (TextView) findViewById(R.id.txtTrack); //text track

        txtTimmer = (Chronometer) findViewById(R.id.txtTimmer); //timmer type Chronometer
        txtTimmer.setBase(SystemClock.elapsedRealtime()); //set type timmer
        txtTimmer.start(); //start timmer

        txtAttempts = (TextView) findViewById(R.id.txtAttempts); //txt attempts
        txtAttempts.setText(String.valueOf(attempts));

        btGuess = (Button) findViewById(R.id.btGuess); //button guess to find number

        btAgain = (Button) findViewById(R.id.btAgain); //button again to gave
        btSaveScore = (Button) findViewById(R.id.btSave); //button save score in ranking activity

        txtFindTheNumber = (EditText) findViewById(R.id.txtFindTheNumber); //edit text where write the number

        try{
            checkDir();
            checkXML();
            readXML();
        }catch (Exception e){
            e.printStackTrace();
        }

        setRankingDialog();

        btGuess.setOnClickListener(new View.OnClickListener() { //when click one time the button guess
            @Override
            public void onClick(View v) {
                if(txtFindTheNumber.getText().toString().length() != 0) { //if edit text not null check the number
                    //if user found the number send a msg in track area
                    if (randomNumber == Integer.valueOf(txtFindTheNumber.getText().toString()) && Integer.valueOf(txtFindTheNumber.getText().toString()) >= 0 && Integer.valueOf(txtFindTheNumber.getText().toString()) <= 100) {
                        attempts++;
                        txtAttempts.setText(String.valueOf(attempts)); // up 1 to attempts, and set attempts in text view

                        txtTrack.setText("CONGRATULATIONS YOU FOUND THE NUMBER!!!");
                        txtTimmer.stop(); //when user found the number chronomter to stop

                        timeUser = String.valueOf(totalTime(txtTimmer.getText().toString()));

                        btGuess.setEnabled(false); //set not enable button guess
                        btAgain.setEnabled(true); //set enable button guess
                        btSaveScore.setEnabled(true); //set enable button guess
                    }
                    //else if user write the number greater to number find
                    else if (randomNumber > Integer.valueOf(txtFindTheNumber.getText().toString()) && Integer.valueOf(txtFindTheNumber.getText().toString()) >= 0 && Integer.valueOf(txtFindTheNumber.getText().toString()) <= 100) {
                        txtTrack.setText("THE NUMBER IS GREATER THAT " + txtFindTheNumber.getText().toString()); //send to msg in track area, "the number is greater than"

                        attempts++;
                        txtAttempts.setText(String.valueOf(attempts)); // up 1 to attempts, and set attempts in text view
                    }
                    //else if user write the number small to number find and
                    else if (randomNumber < Integer.valueOf(txtFindTheNumber.getText().toString()) && Integer.valueOf(txtFindTheNumber.getText().toString()) >= 0 && Integer.valueOf(txtFindTheNumber.getText().toString()) <= 100) {
                        txtTrack.setText("THE NUMBER IS SMALL THAT " + txtFindTheNumber.getText().toString()); // send to msg in track area, "the number is small than"

                        attempts++;
                        txtAttempts.setText(String.valueOf(attempts)); // up 1 to attempts, and set attempts in text view
                    }
                    //else, if the user types a number greater than 100 and a number smaller than 0 sends a message in the track area, "the number is between"
                    else {
                        txtTrack.setText("THE NUMBER IS BETWEEN 0 and 100");

                        attempts++;
                        txtAttempts.setText(String.valueOf(attempts)); // up 1 to attempts, and set attempts in text view
                    }
                }
                else{ //if edit text is null, send a track
                    txtTrack.setText("INSERT THE NUMBER BETWEEN 0 and 100");
                }
            }
        });

        btSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRankingDialog();
            }
        });

        btAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btGuess.setEnabled(true); //set enable button guess
                btAgain.setEnabled(false); //set not enable button guess
                btSaveScore.setEnabled(false); //set not enable button guess

                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //start again main activity
            }
        });
    }

    public void checkDir(){
        if(!filesDir.exists()){
            filesDir.mkdir();
            filePhotoDir.mkdir();
            return;
        }

        if(!filePhotoDir.exists()){
            filePhotoDir.mkdir();
        }
    }

    public void checkXML(){
        try{
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();

            if(!fileRanking.exists()){
                domi = db.getDOMImplementation();

                rankingDoc = domi.createDocument(null, "ranking", null);
                rankingDoc.setXmlVersion("1.0");

                Source source = new DOMSource(rankingDoc);
                Result result = new StreamResult(fileRanking);

                transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source,result);
            }
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void readXML(){
        usersList.clear();

        try{
            rankingDoc = db.parse(fileRanking);
            NodeList recordsNodeList = rankingDoc.getElementsByTagName("user");

            for(int i = 0; i < recordsNodeList.getLength(); i++){
                Node nodeRecords = recordsNodeList.item(i);
                NodeList recordsDataNodeList = nodeRecords.getChildNodes();

                for(int j = 0; j < recordsDataNodeList.getLength(); j++){
                    Node recordDataNode = recordsDataNodeList.item(j);

                    switch (recordDataNode.getNodeName()){
                        case "name":
                            nameUser = recordDataNode.getTextContent();
                            break;

                        case "attempts":
                            attemptsUser = Integer.valueOf(recordDataNode.getTextContent());
                            break;

                        case "time":
                            timeUser = recordDataNode.getTextContent();
                            break;

                        case "photo":
                            photoFile = new File(recordDataNode.getTextContent());

                            photoUser = BitmapFactory.decodeStream(new FileInputStream(photoFile));
                            break;
                    }
                }

                usersList.add(new User(nameUser, attemptsUser, timeUser, photoUser));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setRankingDialog() {
        adb = new AlertDialog.Builder(this);

        adb.setTitle("Do you want to play again?");
        adb.setMessage("Introduce your name");

        txtName = new EditText(this);

        adb.setView(txtName);

        adb.setPositiveButton("YES", null);
        //adb.setNegativeButton("NO", null);

        adRanking = adb.create();

        adRanking.setCancelable(false);
        adRanking.setCanceledOnTouchOutside(false);
    }

    private void showRankingDialog(){
        adRanking.show();

        Button btYes = adRanking.getButton(AlertDialog.BUTTON_POSITIVE);
        //Button btNo = adRanking.getButton(AlertDialog.BUTTON_NEGATIVE);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtName.getText().toString().equals("")){
                    nameUser = txtName.getText().toString();

                    txtName.setText("");

                    adRanking.dismiss();

                    takePhoto();
                    //startActivity(new Intent(MainActivity.this, RankingActivity.class));
                }
            }
        });
    }

    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(takePictureIntent, 1);
    }

    public int totalTime(String time){
        String[] timeSplit = time.split(":");

        int min = Integer.valueOf(timeSplit[0]);
        int sec = Integer.valueOf(timeSplit[1]);

        if(min == 0){
            return sec;
        }
        else{
            int minToSec = min*60;
            int totalTime = minToSec+sec;

            return totalTime;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == 1 && resultCode == RESULT_OK){
            Bundle extras = intent.getExtras();
            photoUser = (Bitmap) extras.get("data");

            savePhoto();

            newUser = new User(nameUser, attemptsUser, timeUser, photoUser);

            usersList.add(getRightIndex(), newUser);

            writeXML();

            startActivity(new Intent(MainActivity.this, RankingActivity.class));
        }
    }

    private void savePhoto(){
        photoFile = new File(filePhotoDir +"/"+ nameUser + extPhoto);

        try{
            if(!photoFile.exists()){
                photoFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(photoFile);
            photoUser.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getRightIndex(){
        int infLimit = 0;
        int supLimit = usersList.size()-1;
        int indexSearch;

        int infRecordCompare;
        int midRecordCompare;
        int supRecordComapre;

        while(infLimit <= supLimit){
            indexSearch = (infLimit + supLimit)/2;

            midRecordCompare = newRecordCompare(indexSearch);

            if(usersList.size() <= 2){
                if(midRecordCompare > 0){
                    return usersList.size();
                }

                return indexSearch;
            }

            if(indexSearch == 0 || indexSearch == usersList.size()-1){
                if(midRecordCompare > 0){
                    return indexSearch+1;
                }

                return indexSearch;
            }

            if(midRecordCompare == 0){
                return indexSearch+1;
            }
            else{
                infRecordCompare = newRecordCompare(indexSearch-1);
                supRecordComapre = newRecordCompare(indexSearch+1);
            }

            if(infRecordCompare >= 0 && supRecordComapre <= 0){
                if(midRecordCompare < 0){
                    return indexSearch;
                }

                return indexSearch+1;
            }
            else if(infRecordCompare > 0){
                infLimit = indexSearch+1;
            }
            else if(supRecordComapre < 0){
                supLimit = indexSearch-1;
            }
        }

        return 0;
    }

    public int newRecordCompare(int indexSearch){
        return newUser.compareRecords(usersList.get(indexSearch));
    }

    private void writeXML(){
        fileRanking.delete();

        domi = db.getDOMImplementation();

        rankingDoc = domi.createDocument(null, "ranking", null);
        rankingDoc.setXmlVersion("1.0");

        try{
            for(int i = 0; i < usersList.size(); i++){
                Element elementUser = rankingDoc.createElement("user");

                Element elementName = rankingDoc.createElement("name");
                Text textName = rankingDoc.createTextNode(usersList.get(i).getName());

                elementName.appendChild(textName);

                Element elementAttempts = rankingDoc.createElement("attempts");
                Text textAttempts = rankingDoc.createTextNode(String.valueOf(usersList.get(i).getAttempts()));

                elementAttempts.appendChild(textAttempts);

                Element elementTime = rankingDoc.createElement("time");
                Text textTime = rankingDoc.createTextNode(usersList.get(i).getName());

                elementTime.appendChild(textTime);

                Element elementPhoto = rankingDoc.createElement("photo");
                Text textPhoto = rankingDoc.createTextNode(filePhotoDir +"/" + usersList.get(i).getName() + extPhoto);

                elementPhoto.appendChild(textPhoto);

                elementUser.appendChild(elementName);
                elementUser.appendChild(elementAttempts);
                elementUser.appendChild(elementTime);
                elementUser.appendChild(elementPhoto);

                rankingDoc.getDocumentElement().appendChild(elementUser);
            }

            Source source = new DOMSource(rankingDoc);
            Result result = new StreamResult(fileRanking);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}