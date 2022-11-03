import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WhatsMessageBatchDetailComponent } from './whats-message-batch-detail.component';

describe('WhatsMessageBatch Management Detail Component', () => {
  let comp: WhatsMessageBatchDetailComponent;
  let fixture: ComponentFixture<WhatsMessageBatchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WhatsMessageBatchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ whatsMessageBatch: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WhatsMessageBatchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WhatsMessageBatchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load whatsMessageBatch on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.whatsMessageBatch).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
