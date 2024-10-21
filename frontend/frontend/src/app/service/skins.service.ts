import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { OAuthService } from "angular-oauth2-oidc";
import { Skin } from "../modelli/Skin";

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type':  'application/json',
    })
};

@Injectable({
    providedIn: 'root'
})

export class SkinsService {
    private url: string = 'http://localhost:8080/skins/'

    constructor(private oauthService: OAuthService,private httpClient: HttpClient) {}

    possedute(): Observable<any> {
        return this.httpClient.get<any>(this.url + 'owned', { 
            headers: { 
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            }})
    }

    nonPossedute(): Observable<any> {
        return this.httpClient.get<any>(this.url + 'notowned', {
            headers: { 
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            }})
    }

    acquire(params: HttpParams): Observable<any> {
        return this.httpClient.post<any>(this.url + 'acquire', {
            params: params,    
            headers: { 
              'Authorization': `Bearer ${this.oauthService.getAccessToken()}`,
            }})
    }

    add() {

    } 
}