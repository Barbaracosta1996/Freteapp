import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../frota.test-samples';

import { FrotaFormService } from './frota-form.service';

describe('Frota Form Service', () => {
  let service: FrotaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrotaFormService);
  });

  describe('Service methods', () => {
    describe('createFrotaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFrotaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            modelo: expect.any(Object),
            marca: expect.any(Object),
            ano: expect.any(Object),
            tipoCategoria: expect.any(Object),
            obsCategoriaOutro: expect.any(Object),
            perfil: expect.any(Object),
            categoriaVeiculo: expect.any(Object),
          })
        );
      });

      it('passing IFrota should create a new form with FormGroup', () => {
        const formGroup = service.createFrotaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            modelo: expect.any(Object),
            marca: expect.any(Object),
            ano: expect.any(Object),
            tipoCategoria: expect.any(Object),
            obsCategoriaOutro: expect.any(Object),
            perfil: expect.any(Object),
            categoriaVeiculo: expect.any(Object),
          })
        );
      });
    });

    describe('getFrota', () => {
      it('should return NewFrota for default Frota initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFrotaFormGroup(sampleWithNewData);

        const frota = service.getFrota(formGroup) as any;

        expect(frota).toMatchObject(sampleWithNewData);
      });

      it('should return NewFrota for empty Frota initial value', () => {
        const formGroup = service.createFrotaFormGroup();

        const frota = service.getFrota(formGroup) as any;

        expect(frota).toMatchObject({});
      });

      it('should return IFrota', () => {
        const formGroup = service.createFrotaFormGroup(sampleWithRequiredData);

        const frota = service.getFrota(formGroup) as any;

        expect(frota).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFrota should not enable id FormControl', () => {
        const formGroup = service.createFrotaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFrota should disable id FormControl', () => {
        const formGroup = service.createFrotaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
