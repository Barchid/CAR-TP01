# -----------------------------------------------------------------------
# Extraction d'attributs de pixels pour la classification,
# Module RdF, reconnaissance de formes
# Copyleft (C) 2014, Universite Lille 1
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
# -----------------------------------------------------------------------

# Chargement des fonctions externes
library ("EBImage")
source ("rdfSegmentation.R")

# Chargement d'une image
# nom <- "rdf-2-classes-texture-0.png"
nom <- "rdf-2-classes-texture-1.png"
# nom <- "rdf-2-classes-texture-2.png"
# nom <- "rdf-2-classes-texture-3.png"
# nom <- "rdf-2-classes-texture-4.png"
image <- rdfReadGreyImage (nom)
reference = rdfReadGreyImage("rdf-masque-ronds.png")

# Calcul et affichage de son histogramme
nbins <- 64

nivTexture = rdfTextureEcartType(image, 2)

h <- hist (nivTexture, breaks = seq (0, 1, 1 / nbins))

display (nivTexture, "niveau de texture", method = "raster", all = TRUE)

his2d = rdfCalculeHistogramme2D(image, 256, nivTexture, 256)
display (his2d, "niveau de texture", method = "raster", all = TRUE)
lines(x = c(130, 155), y = c(0, 255), col = "green")

a = -0.1
b = 0.1
c = 0

binaire = a * image + b * nivTexture + c < 0

display (binaire, "binarisation", method = "raster", all = TRUE)