import {Component, Input, OnInit} from '@angular/core';
import {IPerfil} from "../../../entities/perfil/perfil.model";
import {Account} from "../../../core/auth/account.model";
import {TipoConta} from "../../../entities/enumerations/tipo-conta.model";
import {ProfileFormGroup, RegisterProfileFormService} from "../../../portal/criar-perfil/register-profile-form.service";
import {PerfilService} from "../../../entities/perfil/service/perfil.service";
import {AppService} from "../../../core/app/app.service";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {finalize} from "rxjs/operators";

@Component({
  selector: 'jhi-profile-register',
  templateUrl: './profile-register.component.html',
  styleUrls: ['./profile-register.component.scss']
})
export class ProfileRegisterComponent implements OnInit {
  isSaving = false;
  perfil: IPerfil | null = null;

  account: Account | null | undefined;

  @Input()
  tipoConta: TipoConta | undefined;

  editForm: ProfileFormGroup = this.profileFormService.createProfileFormGroup();

  constructor(private profileFormService: RegisterProfileFormService,
              private perfilService: PerfilService,
              private appService: AppService,
              private router: Router) {

  }

  ngOnInit(): void {
    this.account = this.appService.getAccount();
    if (this.tipoConta === TipoConta.MOTORISTA){
      this.editForm = this.profileFormService.createProfileFormGroup();
    } else {
      this.editForm = this.profileFormService.createProfileFormGroupCompany();
    }
  }

  previousState(): void {
    this.router.navigate(['/portal']).then();
  }

  save(): void {
    this.isSaving = true;
    const perfil = this.profileFormService.getPerfil(this.editForm!);
    perfil.tipoConta = this.tipoConta;
    this.subscribeToSaveResponse(this.perfilService.createProfile(perfil));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerfil>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
