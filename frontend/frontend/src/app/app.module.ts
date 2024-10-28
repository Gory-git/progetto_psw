import { NgModule } from '@angular/core';
import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { FormsModule, NgModel } from '@angular/forms';

import { AppRoutingModule } from './app.routes';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormField, MatLabel } from '@angular/material/form-field';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        AppRoutingModule,
        RouterModule,
        CommonModule,
        BrowserAnimationsModule,
        MatDialogModule,
        MatFormField,
        MatLabel,
        FormsModule
    ],
    declarations: [],
    bootstrap: []
})

export class AppModule { }