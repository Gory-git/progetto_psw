import {APP_INITIALIZER, ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { AuthConfig, OAuthService, provideOAuthClient } from 'angular-oauth2-oidc';
import { provideHttpClient } from '@angular/common/http';
import {provideClientHydration} from "@angular/platform-browser";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

export const authCodeFlowConfig: AuthConfig = {
  issuer: 'http://localhost:8180/realms/my-test-realm',
  tokenEndpoint: 'http://localhost:8180/realms/my-test-realm/protocol/openid-connect/token',
  redirectUri: window.location.origin,
  clientId: 'my-webapp-client',
  responseType: 'code',
  scope: 'openid profile',
  showDebugInformation: true,
}

function initializeOauth(oauthService: OAuthService): Promise<void> {
  return new Promise((resolve) => {
    oauthService.configure(authCodeFlowConfig);
    oauthService.setupAutomaticSilentRefresh();
    oauthService.loadDiscoveryDocumentAndLogin()
      .then(() => {
        resolve()
      }); 
  })
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(),
    provideOAuthClient(),
    {
      provide: APP_INITIALIZER,
      useFactory: (oauthService: OAuthService) => {
        return() => {
          initializeOauth(oauthService)
        }
      },
      multi: true,
      deps: [
        OAuthService
      ]
    }, provideAnimationsAsync()
  ]
};
