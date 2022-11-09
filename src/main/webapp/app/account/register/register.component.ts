import { Component, AfterViewInit, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import { RegisterService } from './register.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../../login/login.service';
import { Login } from '../../login/login.model';

import { TipoConta } from '../../entities/enumerations/tipo-conta.model';
import { SettingsContractsService } from '../../entities/settings-contracts/service/settings-contracts.service';
import { ISettingsContracts } from '../../entities/settings-contracts/settings-contracts.model';
import { DataUtils } from '../../core/util/data-util.service';
import { CELLPHONE_MASK, CNPJ_MASK, CPF_MASK, POSTAL_MASK } from '../../shared/input.constants';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.scss'],
})
export class RegisterComponent implements AfterViewInit, OnInit {
  tipoConta = TipoConta.MOTORISTA;

  phoneMask = CELLPHONE_MASK;
  cpfMask = CPF_MASK;
  cnpjMask = CNPJ_MASK;
  cepMask = POSTAL_MASK;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;
  contractTerms: ISettingsContracts | null | undefined;

  registerForm = new FormGroup({
    login: new FormControl(''),
    firstName: new FormControl('', { validators: [Validators.required, Validators.maxLength(50)] }),
    lastName: new FormControl('', { validators: [Validators.required, Validators.maxLength(50)] }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    telephoneNumber: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(14), Validators.maxLength(15)],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    tipoConta: new FormControl('', {}),
    cpf: new FormControl('', {}),
    cnpj: new FormControl('', {}),
    rua: new FormControl('', {}),
    numero: new FormControl('', {}),
    bairro: new FormControl('', {}),
    cidade: new FormControl('', {}),
    estado: new FormControl('', {}),
    cep: new FormControl('', {}),
    pais: new FormControl('', {}),
    nome: new FormControl('', {}),
    razaosocial: new FormControl('', {}),
    telefoneComercial: new FormControl('', {
      validators: [Validators.minLength(14), Validators.maxLength(15)],
    }),
    acceptedTerm: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  constructor(
    private registerService: RegisterService,
    public router: Router,
    private loginService: LoginService,
    protected activatedRoute: ActivatedRoute,
    private settingsContractsService: SettingsContractsService,
    protected dataUtils: DataUtils
  ) {}

  ngAfterViewInit(): void {}

  ngOnInit() {
    this.settingsContractsService.getContract().subscribe(contract => {
      console.log(contract);
      if (contract.status === 200 && contract.body) {
        this.contractTerms = contract.body;
      }
    });

    this.activatedRoute.data.subscribe(({ tipo }) => {
      this.tipoConta = tipo;
    });
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const { password, confirmPassword } = this.registerForm.getRawValue();
    if (password !== confirmPassword) {
      this.doNotMatch = true;
    } else {
      let user = this.registerForm.getRawValue();
      let login = user.email;

      const perfil: any = {};

      perfil.nome = user.firstName + ' ' + user.lastName;
      perfil.cep = user.cep || null;
      perfil.razaosocial = user.razaosocial || user.nome;
      perfil.telefoneComercial = user.telefoneComercial?.replace(/\D/g, '');
      perfil.cnpj = user.cnpj || null;
      perfil.cpf = user.cpf;
      perfil.cidade = user.cidade || null;
      perfil.estado = user.estado || null;
      perfil.numero = user.numero || null;
      perfil.pais = 'br';
      perfil.tipoConta = this.tipoConta;

      this.registerService
        .save({
          login: user.email,
          email: user.email,
          password,
          telephoneNumber: user.telephoneNumber.replace(/\D/g, ''),
          langKey: 'pt-br',
          firstName: user.firstName,
          lastName: user.firstName,
          perfil: perfil,
        })
        .subscribe({
          next: () => this.autentication({ username: login!, password: password, rememberMe: true }),
          error: response => this.processError(response),
        });
    }
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }

  autentication(login: Login): void {
    this.loginService.login(login).subscribe({
      next: () => {
        if (!this.router.getCurrentNavigation()) {
          this.router.navigate(['']).then();
        }
      },
    });
  }

  openFile(base64String?: string, contentType?: string | null | undefined): void {
    if (base64String && contentType) {
      return this.dataUtils.openFile(base64String, contentType);
    }
  }
}
