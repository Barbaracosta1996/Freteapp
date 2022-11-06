import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PasswordStrengthBarComponent } from './password/password-strength-bar/password-strength-bar.component';
import { RegisterComponent } from './register/register.component';
import { ActivateComponent } from './activate/activate.component';
import { PasswordComponent } from './password/password.component';
import { PasswordResetInitComponent } from './password-reset/init/password-reset-init.component';
import { PasswordResetFinishComponent } from './password-reset/finish/password-reset-finish.component';
import { SettingsComponent } from './settings/settings.component';
import { accountState } from './account.route';
import { ProfileRegisterComponent } from './register/profile-register/profile-register.component';
import { InputTextModule } from 'primeng/inputtext';
import { InputMaskModule } from 'primeng/inputmask';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { CheckboxModule } from 'primeng/checkbox';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { TabMenuModule } from 'primeng/tabmenu';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(accountState),
    InputTextModule,
    InputMaskModule,
    ButtonModule,
    RippleModule,
    CheckboxModule,
    BreadcrumbModule,
    TabMenuModule,
  ],
  declarations: [
    ActivateComponent,
    RegisterComponent,
    PasswordComponent,
    PasswordStrengthBarComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    ProfileRegisterComponent,
  ],
  exports: [RegisterComponent, PasswordStrengthBarComponent, PasswordComponent],
})
export class AccountModule {}
