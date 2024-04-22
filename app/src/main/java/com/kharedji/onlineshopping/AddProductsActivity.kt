package com.kharedji.onlineshopping


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kharedji.onlineshopping.domain.models.ProductData

class AddProductsActivity : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase

    private lateinit var selectedImageUri: Uri
    private lateinit var productImage: ImageView
    private lateinit var categorySpinner: Spinner
    private lateinit var priceInput: EditText
    private lateinit var title: EditText
    private lateinit var descriptionInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products)

        // Initialize Firebase components
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize views
        productImage = findViewById(R.id.product_image)
        categorySpinner = findViewById(R.id.category_spinner)
        priceInput = findViewById(R.id.price_input)
        title = findViewById(R.id.product_title)
        descriptionInput = findViewById(R.id.description_input)

        val uploadImageButton: Button = findViewById(R.id.image_upload_button)
        uploadImageButton.setOnClickListener {
            openGallery()
        }

        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            saveProduct()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                productImage.setImageURI(uri)
            }
        }
    }

    private fun saveProduct() {
        val category = categorySpinner.selectedItem.toString()
        val price = priceInput.text.toString()
        val name = title.text.toString()
        val description = descriptionInput.text.toString()

        // Upload image to Firebase Storage
        val storageRef: StorageReference = storage.reference.child("product_images/${selectedImageUri.lastPathSegment}")
        storageRef.putFile(selectedImageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save product data including image URL to Firebase Realtime Database
                    val databaseRef = database.reference.child("products")
                    val id=databaseRef.push().key
                    val productData = ProductData(id!!,name,category, price, description, uri.toString())
                    databaseRef.child(id).setValue(productData)

                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                // Handle error
            }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 1
    }
}
