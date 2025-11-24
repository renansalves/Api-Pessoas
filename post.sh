#!/bin/sh

JSON_FILE="dados.json"
API_ENDPOINT="http://localhost:8080/pessoa/"

# 
# Sobe a carga de pessoas para a api salvara na base de dados
#
jq -c '.Pessoas[]' "$JSON_FILE" | while read -r OBJETO_JSON; do

    curl -s -X POST \
         -H "Content-Type: application/json" \
         -d "$OBJETO_JSON" \
         "$API_ENDPOINT"
done

#
# Lista as pessoas salvas, utilizando o recurso de paginação vai listar as 10 primeiras pessoas da pagina 0. 
#
echo "Pagina 0 e tamanho 10."

curl -X 'GET' \
  'http://localhost:8080/pessoa/?page=0&size=5' \
  -H 'accept: */*' | jq -r '.content'

# Como definimos um tamanho de pagina igual a 10, os ultimos elementos salvos no banco estão na proxima pagina.
echo "Pagina 1 e tamanho 10."
curl -X 'GET' \
  'http://localhost:8080/pessoa/?page=0&size=5' \
  -H 'accept: */*' | jq -r '.content'
