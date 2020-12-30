package com.example.endrevina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RankingActivity extends AppCompatActivity {

    private Button btBack;

    private final File filesDir = new File("/data/user/0/com.example.endrevina/files");;
    private final File filePhotoDir = new File("/data/user/0/com.example.endrevina/files/photos");
    private final File fileRanking = new File("/data/user/0/com.example.endrevina/files/ranking.xml");

    //private final String extPhoto = ".jpeg";

    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document rankingDoc;

    private ArrayList<User> usersList;
    
    private String name;
    private int attempts;
    private String time;
    //private Bitmap photo;

    private RecyclerView recyclerViewRecords;
    private InfoRecords infoRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        btBack = (Button) findViewById(R.id.btBack);

        usersList = new ArrayList<User>();
        
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        //Intent intent = getIntent();

        try{
            preapreReader();
            readXML();
            buildRecyclerView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buildRecyclerView(){
        recyclerViewRecords = findViewById(R.id.records);
        recyclerViewRecords.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        infoRecords = new InfoRecords(usersList);
        recyclerViewRecords.setAdapter(infoRecords);
    }

    public void preapreReader(){
        try{
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readXML(){
        File photoFile;

        Bitmap photoBitmap = null;

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
                            name = recordDataNode.getTextContent();
                            break;

                        case "attempts":
                            attempts = Integer.valueOf(recordDataNode.getTextContent());
                            break;

                        case "time":
                            time = recordDataNode.getTextContent();
                            break;

                        case "photo":
                            photoFile = new File(recordDataNode.getTextContent());

                            photoBitmap = BitmapFactory.decodeStream(new FileInputStream(photoFile));
                            break;
                    }
                }

                usersList.add(new User(name, attempts, time, photoBitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}