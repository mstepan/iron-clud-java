# Java Security Socket Extension example.

## Debugging.
```
-Djavax.net.debug=ssl
```

## Keystore/truststore setup.

### Generate private-public key pair.
```
keytool -genkey -alias localhost-selfsigned -keyalg RSA -keypass 611191 -storepass 611191 -keystore server_keystore.jks -validity 360 -keysize 2048
```

### Export public key to an external file.
```
keytool -export -alias localhost-selfsigned -keystore server_keystore.jks -rfc -file public_key.cer
```

### Import public key file to a truststore.
```
keytool -import -alias localhost-selfsigned -file public_key.cer -storetype JKS -keystore client_truststore.jks
```


## Tink & Tinkey

###  Generate new key with Tinkey and store in file `my_keyset.json`.
```
tinkey create-keyset --key-template AES256_GCM --out my_keyset.json
```

### Rotate key with Tinkey.
```
./rotate-keys.sh

```

