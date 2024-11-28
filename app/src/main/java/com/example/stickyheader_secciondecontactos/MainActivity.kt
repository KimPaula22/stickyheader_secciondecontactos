package com.example.stickyheader_secciondecontactos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stickyheader_secciondecontactos.ui.theme.StickyHeaderSecciónDeContactosTheme

// Modelo de datos
data class Contacto(
    val nombre: String,
    val telefono: String,
    val esFavorito: Boolean = false
)

// Lista de contactos de ejemplo
val contactos = listOf(
    Contacto("Ana", "123456789", esFavorito = true),
    Contacto("Alberto", "987654321"),
    Contacto("Carlos", "112233445"),
    Contacto("Claudia", "556677889"),
    Contacto("David", "998877665", esFavorito = true),
    Contacto("Eva", "223344556"),
    Contacto("Elena", "667788990")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StickyHeaderSecciónDeContactosTheme {
                // Scaffold para aplicar padding adecuado
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaContactos(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Función para mostrar los contactos
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaContactos(modifier: Modifier = Modifier) {
    // Separar contactos favoritos y los demás
    val favoritos = contactos.filter { it.esFavorito }
    val otrosContactos = contactos.filter { !it.esFavorito }
        .groupBy { it.nombre.first().uppercase() }
        .toSortedMap() // Ordenar las claves alfabéticamente

    LazyColumn(modifier = modifier) {
        // Sección de favoritos
        if (favoritos.isNotEmpty()) {
            stickyHeader {
                Text(
                    text = "★ Favoritos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFC0CB)) // Fondo rosa para la sección
                        .padding(8.dp),
                    color = Color.White
                )
            }
            items(favoritos) { contacto ->
                ContactoItem(contacto)
            }
        }

        // Secciones para contactos agrupados por inicial
        otrosContactos.forEach { (inicial, listaContactos) ->
            stickyHeader {
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFC0CB)) // Fondo rosa para las iniciales
                        .padding(8.dp),
                    color = Color.White
                )
            }
            items(listaContactos) { contacto ->
                ContactoItem(contacto)
            }
        }
    }
}

// Generar un color basado en el nombre del contacto
fun generarColorParaContacto(nombre: String): Color {
    val hash = nombre.hashCode()
    return Color(
        red = (hash shr 16 and 0xFF) / 255f,
        green = (hash shr 8 and 0xFF) / 255f,
        blue = (hash and 0xFF) / 255f
    )
}

// Componente para mostrar la información de un contacto
@Composable
fun ContactoItem(contacto: Contacto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        // Ícono de color basado en la inicial del contacto
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(generarColorParaContacto(contacto.nombre), shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contacto.nombre.first().uppercase(),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Nombre y teléfono del contacto
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contacto.nombre, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = contacto.telefono, style = MaterialTheme.typography.bodyMedium)
        }

        // Mostrar estrella si es favorito
        if (contacto.esFavorito) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Favorito",
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 8.dp),
                tint = Color.Yellow
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StickyHeaderSecciónDeContactosTheme {
        // Llamamos a la pantalla de contactos para mostrar el preview
        PantallaContactos()
    }
}