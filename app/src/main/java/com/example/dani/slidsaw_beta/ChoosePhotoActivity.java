package com.example.dani.slidsaw_beta;

/**
 * Created by dani on 16/12/15.
 */

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ChoosePhotoActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    // Variables:
    final int CAMERA_CAPTURE = 1;
    final int GALLERY_CAPTURE = 2;
    final int PIC_CROP = 3;
    int RES_CODE_BACK = 0;
    private Uri picUri;
    private String name = "";
    String message;
    Toast toast;

    // Variables de widgets:
    ImageButton btn_camera;
    ImageButton btn_gallery;
    Button btn_play;
    ImageView imageView;
    public static Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);

        Typeface dejavu = TypefacesUtils.get(this, "fonts/Roboto-Light.ttf");


        GameView.first = true;
        // Enllaç objectes java amb objectes xml:
        btn_camera = (ImageButton) findViewById(R.id.btn_camera);
        btn_gallery = (ImageButton) findViewById(R.id.btn_gallery);
        btn_play = (Button) findViewById(R.id.btn_play);
        imageView = (ImageView) findViewById(R.id.imageView);

        btn_play.setTypeface(dejavu);

        // onClickListeners per cada widget:
        btn_camera.setOnClickListener(this);
        btn_gallery.setOnClickListener(this);
        btn_play.setOnClickListener(this);

        // Amaguem botó play fins que tinguem una imatge
        btn_play.setVisibility(View.GONE);

        // onLongClickListeners per cada widget:
        btn_camera.setOnLongClickListener(this);
        btn_gallery.setOnLongClickListener(this);
        btn_play.setOnLongClickListener(this);

        name = Environment.getExternalStorageDirectory() + "/test.jpg";

        if(imageBitmap != null){
            imageView.setImageBitmap(imageBitmap);
            btn_play.setVisibility(View.VISIBLE);
        }

    }

    // Métodes per a cada botó:
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Si escollim fer la fotografía amb la càmara del nostre dispossitiu:
            case R.id.btn_camera:
                try {
                    // Intent standar per capturar una imatge
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Esperarem una resposta d'aquest intent a onActivityResult
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                }
                catch(ActivityNotFoundException anfe){
                    // Si hi ha un error, mostrar missatge:
                    String errorMessage = "No es pot obrir la càmara";
                    toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            // Si escollim obrirla desde la galería del nostre dispossitiu
            case R.id.btn_gallery:
                try {
                    // Intent standar per obrir la galería de fotos d'android:
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    // Esperarem una resposta d'aquest intent a onActivityResult
                    startActivityForResult(intent, GALLERY_CAPTURE);
                }
                catch(ActivityNotFoundException anfe){
                    // Si hi ha un error, mostrar missatge:
                    String errorMessage = "No es troba la galería";
                    toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            // Si pitgem el botó btn_play
            case R.id.btn_play:
                try {
                    // Intent per anar a la següent activitat per començar a jugar:
                    Intent intent = new Intent(this, MainActivity.class);
                    // Creem un strem de bytes de sortida per enviar
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    // Comprimim imatgeBitmap al ByteOutStream 'bs'
                    imageBitmap.compress(Bitmap.CompressFormat.PNG,100, bs);
                    // Afegim el 'imageBitmap a
                    intent.putExtra("byteArray", bs.toByteArray());
                    startActivity(intent);
                    GameView.first = true;
                    overridePendingTransition(R.animator.zoom_back_in, R.animator.zoom_back_out);
                }
                catch(ActivityNotFoundException anfe){
                    // Si hi ha un error, mostrar missatge:
                    String errorMessage = "No es pot enviar la foto a la següent activitat: " + anfe;
                    toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }
    }

    // Métode per rebre respostes dels intents
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Si el codi retornat ve de la càmara:
            case CAMERA_CAPTURE:
                if(resultCode == RESULT_OK){picUri = data.getData(); // Obtenim URI de la imatge
                    RES_CODE_BACK = CAMERA_CAPTURE;
                    performCrop(); // Cridem a la funció performCrop d'Android
                }
                break;
            // Si el codi retornat ve de la galería android
            case GALLERY_CAPTURE:
                if(resultCode == RESULT_OK) {
                    picUri = data.getData(); // Obtenim URI de la imatge
                    RES_CODE_BACK = GALLERY_CAPTURE;
                    performCrop(); // Cridem a la funció performCrop() d'Android
                }
                break;
            // Si el codi retornat ve de la funció retallar:
            case PIC_CROP:
                if(resultCode == RESULT_OK) {
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    imageBitmap = extras.getParcelable("data");
                    //display the returned cropped image
                    imageView.setImageBitmap(imageBitmap);
                    btn_play.setVisibility(View.VISIBLE);
                    break;
                }
                else{
                    retFromCrop(RES_CODE_BACK);
                }
        }
    }

    @Override
    public boolean onLongClick(View v){
        switch (v.getId()){

            // Si escollim fer la fotografía amb la càmara del nostre dispossitiu:
            case R.id.btn_camera:
                message = "Fer foto amb la camara del dispositiu";
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
                break;

            // Si escollim obrirla desde la galería del nostre dispossitiu
            case R.id.btn_gallery:
                message = "Escull una foto de la galeria";
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
                break;

            case R.id.btn_play:
                message = "Comença el joc";
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
        return false;
    }

    // Declaració de finció performCrop():
    private void performCrop(){
        try {
            // Crida a la funció crop de android:
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // Indica el URI de la imatge
            cropIntent.setDataAndType(picUri, "image/*");
            // Configuració de les propietats de crop:
            cropIntent.putExtra("crop", "true");
            // Indica aspecte del retall dessitjat per x i y
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // Indica las sortides x i y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // Retornar dades
            cropIntent.putExtra("return-data", true);
            // Esperem una resposta d'aquest intent a onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            // Si no s'ha pogut efectuar el retall es mostra missatge d'error
            String errorMessage = "Ho sentim, el teu dispossitiu no te funció de crop";
            toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Métode per tornar del crop:
    private void retFromCrop(int code){
        if(code == CAMERA_CAPTURE){
            try {
                // Intent standar per capturar una imatge
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Esperarem una resposta d'aquest intent a onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }
            catch(ActivityNotFoundException anfe){
                // Si hi ha un error, mostrar missatge:
                String errorMessage = "No es pot obrir la càmara";
                toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if(code == GALLERY_CAPTURE){
            try {
                // Intent standar per obrir la galería de fotos d'android:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                // Esperarem una resposta d'aquest intent a onActivityResult
                startActivityForResult(intent, GALLERY_CAPTURE);
            }
            catch(ActivityNotFoundException anfe){
                // Si hi ha un error, mostrar missatge:
                String errorMessage = "No es troba la galería";
                toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, LevelActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.right_out, R.animator.right_in);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

/*    @Override
   public void onPause() {
        super.onPause();
        Intent i = new Intent(this, AudioService.class);
        i.putExtra("action", AudioService.PAUSE);
        startService(i);
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        Intent i = new Intent(this, AudioService.class);
        i.putExtra("action", AudioService.START);
        startService(i);
    }
}