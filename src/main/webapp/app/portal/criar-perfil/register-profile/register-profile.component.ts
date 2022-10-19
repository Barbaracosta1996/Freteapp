import {Component, Input, OnInit} from '@angular/core';
import {TipoConta} from "../../../entities/enumerations/tipo-conta.model";
import {PerfilService} from "../../../entities/perfil/service/perfil.service";
import {IPerfil} from "../../../entities/perfil/perfil.model";
import {ProfileFormGroup, RegisterProfileFormService} from "../register-profile-form.service";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {finalize} from "rxjs/operators";
import {AppService} from "../../../core/app/app.service";
import {Account} from "../../../core/auth/account.model";
import {Router} from "@angular/router";

@Component({
  selector: 'jhi-register-profile',
  templateUrl: './register-profile.component.html',
  styleUrls: ['./register-profile.component.scss']
})
export class RegisterProfileComponent implements OnInit{

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
