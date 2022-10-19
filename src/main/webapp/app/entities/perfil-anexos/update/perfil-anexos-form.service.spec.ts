import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../perfil-anexos.test-samples';

import { PerfilAnexosFormService } from './perfil-anexos-form.service';

describe('PerfilAnexos Form Service', () => {
  let service: PerfilAnexosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PerfilAnexosFormService);
  });

  describe('Service methods', () => {
    describe('createPerfilAnexosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPerfilAnexosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataPostagem: expect.any(Object),
            tipoDocumento: expect.any(Object),
            descricao: expect.any(Object),
            urlFile: expect.any(Object),
            perfil: expect.any(Object),
          })
        );
      });

      it('passing IPerfilAnexos should create a new form with FormGroup', () => {
        const formGroup = service.createPerfilAnexosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataPostagem: expect.any(Object),
            tipoDocumento: expect.any(Object),
            descricao: expect.any(Object),
            urlFile: expect.any(Object),
            perfil: expect.any(Object),
          })
        );
      });
    });

    describe('getPerfilAnexos', () => {
      it('should return NewPerfilAnexos for default PerfilAnexos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPerfilAnexosFormGroup(sampleWithNewData);

        const perfilAnexos = service.getPerfilAnexos(formGroup) as any;

        expect(perfilAnexos).toMatchObject(sampleWithNewData);
      });

      it('should return NewPerfilAnexos for empty PerfilAnexos initial value', () => {
        const formGroup = service.createPerfilAnexosFormGroup();

        const perfilAnexos = service.getPerfilAnexos(formGroup) as any;

        expect(perfilAnexos).toMatchObject({});
      });

      it('should return IPerfilAnexos', () => {
        const formGroup = service.createPerfilAnexosFormGroup(sampleWithRequiredData);

        const perfilAnexos = service.getPerfilAnexos(formGroup) as any;

        expect(perfilAnexos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPerfilAnexos should not enable id FormControl', () => {
        const formGroup = service.createPerfilAnexosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPerfilAnexos should disable id FormControl', () => {
        const formGroup = service.createPerfilAnexosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
