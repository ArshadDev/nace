package com.db.cib.nace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "nace_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaceDataEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "order_code")
    private String order;

    @Column(name = "level")
    private String level;

    @Column(name = "code")
    private String code;

    @Column(name = "parent")
    private String parent;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "item_includes")
    private String itemIncludes;

    @Lob
    @Column(name = "item_also_includes")
    private String itemAlsoIncludes;

    @Lob
    @Column(name = "rulings")
    private String rulings;

    @Lob
    @Column(name = "item_excludes")
    private String itemExcludes;

    @Column(name = "isic_ref")
    private String isicRef;

}
