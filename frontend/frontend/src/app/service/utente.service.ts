import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { OAuthService } from "angular-oauth2-oidc";

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type':  'application/json',
    })
};

@Injectable({
    providedIn: 'root'
})

export class UtenteService {
    private url: string = 'http://localhost:8080/user/'

    constructor(private oauthService: OAuthService,private httpClient: HttpClient) {}

    salva(params: HttpParams): Observable<string> {
        return this.httpClient.post<string>(this.url + 'register', {
            params: params,    
            headers: { 
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            }})
    }
}