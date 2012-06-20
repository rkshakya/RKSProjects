<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Category Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Database ID' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->category->cat_id;?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Code' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="cat_code" id="cat_code" size="32" maxlength="250" value="<?php echo $this->category->cat_code;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Name' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="cat_name" id="cat_name" size="32" maxlength="250" value="<?php echo $this->category->cat_name;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Description' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="cat_desc" id="cat_desc" size="32" maxlength="250" value="<?php echo $this->category->cat_desc;?>" />
			</td>
		</tr>
		
	  <tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Created Date| Modified Date' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->category->cdate;?>|<?php echo $this->category->mdate;?>
			</td>
		</tr>
		
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="cat_id" value="<?php echo $this->category->cat_id; ?>" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="category" />
</form>
