import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { LOGIN_ROUTE } from './login.route';
import { LoginComponent } from './login.component';
import {InputTextModule} from "primeng/inputtext";
import {ButtonModule} from "primeng/button";

@NgModule({
  imports: [SharedModule, RouterModule.forChild([LOGIN_ROUTE]), InputTextModule, ButtonModule],
  declarations: [LoginComponent],
})
export class LoginModule {}
