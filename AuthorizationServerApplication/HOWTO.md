# HowTo

### Password grant request
```
curl abcd:qwerty@localhost:8082/auth/oauth/token -dgrant_type=password -dscope=user_info -dusername=viacheslav -dpassword=pwd
```
### Client credentials
```
curl abcd:qwerty@localhost:8082/auth/oauth/token -dgrant_type=client_credentials -dscope=user_info
```
