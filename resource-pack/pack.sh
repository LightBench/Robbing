#!/bin/bash

# Cartella principale
RESOURCE_PACK_FOLDER="robbing-resource-pack"

# File di input
PACK_FORMAT_FILE="pack-format.yml"

# Cartella di output
BUILD_FOLDER="build"

# Crea la cartella di output se non esiste
mkdir -p "$BUILD_FOLDER"

# Legge il file pack-format.yml riga per riga
while IFS=: read -r key value; do
    # Rimuove gli spazi bianchi
    key=$(echo $key | xargs)
    value=$(echo $value | xargs)

    # Crea il file pack.mcmeta con il valore appropriato
    cat <<EOL > "$RESOURCE_PACK_FOLDER/pack.mcmeta"
{
    "pack": {
        "pack_format": $value,
        "description": "Robbing official texture pack"
    }
}
EOL

    # Crea un archivio zip con il nome della key e include la cartella
    zip -r "${BUILD_FOLDER}/${key}.zip" "$RESOURCE_PACK_FOLDER"

    # Rimuove il file pack.mcmeta dopo aver creato lo zip
    rm "$RESOURCE_PACK_FOLDER/pack.mcmeta"
done < "$PACK_FORMAT_FILE"

echo "Archiviazione completata."
